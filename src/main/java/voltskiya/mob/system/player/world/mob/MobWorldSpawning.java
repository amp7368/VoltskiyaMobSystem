package voltskiya.mob.system.player.world.mob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.biome.BiomeType;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.spawner.LeafSpawner;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.spawn.config.RegenConfig;
import voltskiya.mob.system.storage.mob.DStoredMob;
import voltskiya.mob.system.storage.mob.MobStorage;

public class MobWorldSpawning {

    public static void spawnMobs(Collection<DStoredMob> mobs) {
        List<DStoredMob> mobsToAddBack = new ArrayList<>();
        for (DStoredMob storedMob : mobs) {
            long spawnDelay = trySpawn(storedMob).getSpawnDelay();
            if (spawnDelay > Bukkit.getCurrentTick()) {
                storedMob.setSpawnDelay(spawnDelay);
                mobsToAddBack.add(storedMob);
            }
        }
        MobStorage.insertMobs(mobsToAddBack);
    }

    private static ShouldSpawningResult trySpawn(DStoredMob storedMob) {
        MobType mobType = storedMob.getMobType();

        SpawningContext context = SpawningContext.create(storedMob);
        @Nullable BiomeType biomeType = BiomeDatabases.getBiomeType().get(context.biomeUUID());
        // if the biome has no mobs assigned to it, remove the mob
        if (biomeType == null) return ShouldSpawningResult.SHOULD_REMOVE;
        List<LeafSpawner> leaves = biomeType.getSpawner().getLeaves()
            .stream()
            .filter(s -> s.hasMob(mobType.getId()))
            .toList();
        if (leaves.isEmpty()) return ShouldSpawningResult.SHOULD_REMOVE;
        LeafSpawner spawner = null;
        ShouldSpawningResult ruleResult = null;
        for (LeafSpawner leaf : leaves) {
            spawner = mobType.getBiomeSpawner(leaf);
            ruleResult = spawner.shouldSpawn(context);
            if (ruleResult.shouldSpawn()) break;
        }

        if (ruleResult.shouldSpawn()) {
            boolean skipModifications = !storedMob.isFirstSpawn();
            SpawnerSummonResult summonResult = spawner.prepare(context, skipModifications);
            summonResult.spawn(mobType, skipModifications);
            logMobSummon(storedMob);
        }
        return ruleResult;
    }

    private static void logMobSummon(DStoredMob storedMob) {
        if (!RegenConfig.get().logMobSummon) return;
        Location loc = storedMob.getLocation();
        String mobName = storedMob.getMobType().getName();
        String worldName = loc.getWorld().getName();
        String message = String.format("summon mob %s at %s %d %d %d", mobName, worldName, loc.getBlockX(), loc.getBlockY(),
            loc.getBlockZ());
        VoltskiyaPlugin.get().getLogger().info(message);
    }

}
