package voltskiya.mob.system.spawn.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import voltskiya.apple.utilities.chance.weight.ChanceWeightedChoice;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.biome.BiomeType;
import voltskiya.mob.system.base.mob.MobTypeSpawner;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.spawner.BuiltSpawner;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.storage.mob.DStoredMob;
import voltskiya.mob.system.base.storage.mob.MobStorage;
import voltskiya.mob.system.player.world.mob.ShouldSpawningResult;
import voltskiya.mob.system.spawn.config.MapRegenConfig;
import voltskiya.mob.system.spawn.config.RegenStatsMap;

public class WorldRegenTask implements Runnable {

    private Location locationToTry;
    private BiomeType biomeType;
    private final ChanceWeightedChoice<MobTypeSpawner> random = new ChanceWeightedChoice<>(
        spawner -> spawner.attributesTopLevel().getNormalizedSpawnRate(biomeType.getGlobalSpawnRate()));
    private BuiltSpawner biomeSpawner;
    private boolean isDone = false;

    @Override
    public void run() {
        MobTypeSpawner mobToTry = chooseMobToTry();
        SpawningContext spawnContext = SpawningContext.create(locationToTry);
        ShouldSpawningResult shouldSpawn = mobToTry.spawner().shouldSpawn(spawnContext);
        if (!shouldSpawn.canFutureSpawn()) return;

        DStoredMob spawnedMob = new DStoredMob(mobToTry.mob().getId(), locationToTry);
        spawnedMob.setSpawnDelay(shouldSpawn.getSpawnDelay());
        MobStorage.insertMobs(List.of(spawnedMob));
    }

    public MobTypeSpawner chooseMobToTry() {
        chooseLocation();
        if (isDone()) return null;

        calculateBiomeInfo();
        if (isDone()) return null;

        checkBiomeSpawnerFails();
        if (isDone()) return null;

        Set<MobUUID> mobsInBiome = biomeSpawner.getExtendedByMob();
        List<MobTypeSpawner> spawners = new ArrayList<>();
        for (MobUUID mob : mobsInBiome) {
            MobTypeSpawner mobSpawner = mob.mapped().getSpawner(biomeSpawner);
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
        locationToTry = map.randomLocation();
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
