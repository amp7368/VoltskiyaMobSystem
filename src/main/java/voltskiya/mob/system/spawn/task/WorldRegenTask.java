package voltskiya.mob.system.spawn.task;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import voltskiya.apple.utilities.chance.weight.ChanceWeightedChoice;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.biome.BiomeType;
import voltskiya.mob.system.base.mob.MobTypeSpawnerInBiome;
import voltskiya.mob.system.base.selector.ExtendsMob;
import voltskiya.mob.system.base.spawner.BuiltSpawner;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.ShouldSpawningResult;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.spawn.config.MapRegenConfig;
import voltskiya.mob.system.spawn.config.RegenStatsMap;
import voltskiya.mob.system.storage.mob.DStoredMob;
import voltskiya.mob.system.storage.mob.MobStorage;

public class WorldRegenTask implements Runnable {

    private final Runnable callback;
    private final ChanceWeightedChoice<MobTypeSpawnerInBiome> random = new ChanceWeightedChoice<>(
        spawner -> spawner.extendsMob().spawnWeight);
    private Location locationToTry;
    private BiomeType biomeType;
    private BuiltSpawner biomeSpawner;
    private boolean isDone = false;

    public WorldRegenTask(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            this.doTask();
        } finally {
            callback.run();
        }
    }

    private void doTask() {
        MobTypeSpawnerInBiome mobToTry = chooseMobToTry();
        choosePreciseLocation(mobToTry.spawner());
        SpawningContext spawnContext = SpawningContext.create(locationToTry);
        ShouldSpawningResult shouldSpawn = mobToTry.spawner().shouldSpawn(spawnContext);
        if (!shouldSpawn.canFutureSpawn()) return;

        DStoredMob spawnedMob = new DStoredMob(mobToTry.mob().getId(), locationToTry);
        spawnedMob.setSpawnDelay(shouldSpawn.getSpawnDelay());
        logMobSpawn(spawnedMob);
        MobStorage.insertMobs(List.of(spawnedMob));
    }

    private void logMobSpawn(DStoredMob spawnedMob) {
        Location loc = spawnedMob.getLocation();
        String mobName = spawnedMob.getMobType().getName();
        String worldName = loc.getWorld().getName();
        String message = String.format("create mob %s at %s %d %d %d", mobName, worldName, loc.getBlockX(), loc.getBlockY(),
            loc.getBlockZ());
        ModuleSpawning.get().logger().info(message);
    }

    private void choosePreciseLocation(BuiltSpawner spawner) {

    }

    public MobTypeSpawnerInBiome chooseMobToTry() {
        chooseLocation();
        if (isDone()) return null;

        calculateBiomeInfo();
        if (isDone()) return null;

        checkBiomeSpawnerFails();
        if (isDone()) return null;
        List<ExtendsMob> mobsInBiome = biomeSpawner.getExtendsMob();
        List<MobTypeSpawnerInBiome> spawners = new ArrayList<>();
        for (ExtendsMob extendsMob : mobsInBiome) {
            MobTypeSpawnerInBiome mobSpawner = extendsMob.mob.mapped().getBiomeSpawner(extendsMob, biomeSpawner);

            spawners.add(mobSpawner);
        }
        return this.random.choose(spawners);
    }

    private void calculateBiomeInfo() {
        Biome mcBiome = this.locationToTry.getBlock().getBiome();
        biomeType = BiomeDatabases.getBiomeType().getFromMC(mcBiome.getKey());
        if (biomeType == null) {
            setDone();
            return;
        }
        biomeSpawner = biomeType.getSpawner();
    }

    private void chooseLocation() {
        MapRegenConfig map = RegenStatsMap.chooseMap();
        locationToTry = map.randomGroundLocation();
        if (locationToTry == null) setDone();
    }

    private void checkBiomeSpawnerFails() {
        double globalSpawnRate = BiomeDatabases.getBiomeType().getGlobalSpawnRate();
        double spawnRate = biomeSpawner.attributesTopLevel().getNormalizedSpawnRate(globalSpawnRate);
        if (spawnRate > this.random.random().nextDouble())
            setDone();
    }

    private void setDone() {
        this.isDone = true;
    }

    private boolean isDone() {
        return isDone;
    }

}
