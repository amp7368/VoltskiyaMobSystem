package voltskiya.mob.system.base.biome.database;

import java.util.Comparator;
import java.util.List;
import voltskiya.mob.system.base.biome.BiomeType;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;

public class BiomeTypeDatabase extends AbstractBiomeDatabase<BiomeType> {

    private transient double globalSpawnRate = 0;

    public void loadGlobalStats() {
        globalSpawnRate = this.list().stream().map(biome -> biome.getSpawner().attributesTopLevel().getSpawnRate())
            .max(Comparator.comparingDouble(d -> d)).orElse(1d);
    }

    public double getGlobalSpawnRate() {
        return globalSpawnRate;
    }

    public List<SpawnSelectorGrouping> listSelectors() {
        return list().stream().map(BiomeType::getSpawnerTags).toList();
    }
}
