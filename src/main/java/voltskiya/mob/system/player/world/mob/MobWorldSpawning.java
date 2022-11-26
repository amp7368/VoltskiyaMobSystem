package voltskiya.mob.system.player.world.mob;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.spawner.BiomeSpawner;
import voltskiya.mob.system.base.spawner.MobSpawner;
import voltskiya.mob.system.base.spawner.MobSpawnerFragment;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.storage.mob.MobStorage;
import voltskiya.mob.system.base.storage.mob.StoredMob;
import voltskiya.mob.system.player.world.watch.WatchPlayerConfig;

public class MobWorldSpawning {

    public static void spawnMobs(List<StoredMob> mobs) {
        List<StoredMob> mobsToAddBack = new ArrayList<>();
        for (StoredMob storedMob : mobs) {
            long spawnDelay = trySpawn(storedMob).getSpawnDelay();
            if (spawnDelay > 0) {
                storedMob.setSpawnDelay(spawnDelay);
                mobsToAddBack.add(storedMob);
            }
        }
        MobStorage.insertMobs(mobsToAddBack);
    }

    private static ShouldSpawningResult trySpawn(StoredMob storedMob) {
        MobUUID mobUUID = storedMob.getMobUUID();
        MobType mobType = storedMob.getMobType();

        SpawningContext context = SpawningContext.create(storedMob);
        BiomeSpawner biomeType = BiomeDatabases.getSpawning().get(context.biomeUUID());
        // if the biome has no mobs assigned to it, remove the mob
        if (biomeType == null) return ShouldSpawningResult.SHOULD_REMOVE;
        MobSpawnerFragment biomeSpawning = biomeType.getSpawner(mobUUID);
        MobSpawnerFragment globalSpawning = mobType.getSpawner();
        MobSpawner spawner = new MobSpawner(biomeSpawning, globalSpawning);
        ShouldSpawningResult ruleResult = spawner.shouldSpawn(context);

        if (ruleResult.shouldSpawn()) {
            SpawnerSummonResult summonResult = spawner.spawnMob(context);
            mobType.spawn(summonResult.getLocation(), summonResult::modifyEntity);
            logMobSpawn(storedMob);
        }
        return ruleResult;
    }

    private static void logMobSpawn(StoredMob storedMob) {
        if (!WatchPlayerConfig.get().showSummonMob) return;
        Location loc = storedMob.getLocation();
        String mobName = storedMob.getMobType().getName();
        String worldName = loc.getWorld().getName();
        String message = String.format("summon mob %s at %s %d %d %d", mobName, worldName, loc.getBlockX(), loc.getBlockY(),
            loc.getBlockZ());
        VoltskiyaPlugin.get().getLogger().info(message);
    }

}
