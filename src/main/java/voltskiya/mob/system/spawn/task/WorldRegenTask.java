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
import voltskiya.mob.system.spawn.config.MapRegenConfig;
import voltskiya.mob.system.spawn.config.RegenStatsMap;

public class WorldRegenTask implements Runnable {

    private final ChanceWeightedChoice<MobTypeSpawner> random = new ChanceWeightedChoice<>(MobTypeSpawner::getSpawnRate);
    private Location locationToTry;
    private BiomeType biomeType;
    private BuiltSpawner biomeSpawner;
    private boolean isDone = false;

    @Override
    public void run() {
        MobTypeSpawner mobToTry = chooseMobToTry();
        // todo try to spawn the mob
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
        MobTypeSpawner choice = this.random.choose(spawners);
        return choice;
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
        if (biomeSpawner.getSpawnRate() < this.random.random().nextDouble())
            setDone();
    }

    private void setDone() {
        this.isDone = true;
    }

    private boolean isDone() {
        return isDone;
    }

}