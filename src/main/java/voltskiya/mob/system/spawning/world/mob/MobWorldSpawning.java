package voltskiya.mob.system.spawning.world.mob;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.biome.BiomeTypeDatabase;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.spawning.context.SpawningContext;
import voltskiya.mob.system.spawning.spawner.BiomeSpawning;
import voltskiya.mob.system.spawning.spawner.MobTypeSpawner;
import voltskiya.mob.system.spawning.storage.MobStorage;
import voltskiya.mob.system.spawning.storage.StoredMob;
import voltskiya.mob.system.spawning.world.watch.WatchPlayerConfig;

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
        BiomeSpawning biomeType = BiomeTypeDatabase.getSpawning().get(context.biomeUUID());
        // if the biome has no mobs assigned to it, remove the mob
        if (biomeType == null)
            return ShouldSpawningResult.SHOULD_REMOVE;
        MobTypeSpawner biomeSpawning = biomeType.getSpawner(mobUUID);
        MobTypeSpawner mobSpawning = mobType.getSpawner();
        ShouldSpawningResult ruleResult = shouldSpawn(context, biomeSpawning, mobSpawning);
        if (ruleResult.shouldSpawn()) {
            spawnMob(context, mobType, biomeSpawning, mobSpawning);
            logMobSpawn(storedMob);
        }
        return ruleResult;
    }

    private static void spawnMob(SpawningContext context, MobType mobType,
        MobTypeSpawner... spawners) {
        SpawnerSummonResult result = new SpawnerSummonResult(context);
        for (MobTypeSpawner spawner : spawners)
            spawner.preModify(context, result);
        for (MobTypeSpawner spawner : spawners)
            spawner.modify(context, result);
        mobType.spawn(result.getLocation(), result::modifyEntity);
    }

    private static ShouldSpawningResult shouldSpawn(SpawningContext context,
        MobTypeSpawner... spawners) {
        ShouldSpawningResult result = new ShouldSpawningResult();
        for (MobTypeSpawner spawner : spawners) {
            if (spawner == null || spawner.isBreaksRule(context))
                return ShouldSpawningResult.SHOULD_REMOVE;
            result.delayUtil(spawner.spawnDelay(context));
        }
        return result;
    }

    private static void logMobSpawn(StoredMob storedMob) {
        if (!WatchPlayerConfig.get().showSummonMob)
            return;
        Location loc = storedMob.getLocation();
        String message = String.format("summon mob %s at %s %d %d %d",
            storedMob.getMobType().getName(), loc.getWorld().getName(), loc.getBlockX(),
            loc.getBlockY(), loc.getBlockZ());
        VoltskiyaPlugin.get().getLogger().info(message);
    }

}
