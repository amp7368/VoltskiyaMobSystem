package voltskiya.mob.system.base.biome.database;

import java.util.Comparator;
import java.util.List;
import voltskiya.mob.system.base.biome.BiomeType;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;

public class BiomeTypeDatabase extends AbstractBiomeDatabase<BiomeType> {

    private transient double globalSpawnRate = 0;

    public void loadGlobalStats() {
        globalSpawnRate = this.list()
            .stream()
            .map(biome -> biome.getSpawner().getMaxSpawnRate())
            .max(Comparator.comparingDouble(s -> s)).orElse(1d);
    }

    public double getGlobalSpawnRate() {
        return globalSpawnRate;
    }

    public List<SpawnSelectorGrouping> listSelectors() {
        return list().stream().map(BiomeType::getSpawnerTags).toList();
    }

    public double normalizedSpawnRate(double spawnRate) {
        if (globalSpawnRate == 0) return 0;
        return spawnRate / globalSpawnRate;
    }
}
