package voltskiya.mob.system.base.biome.database;

import java.util.List;
import voltskiya.mob.system.base.biome.BiomeType;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;

public class BiomeTypeDatabase extends AbstractBiomeDatabase<BiomeType> {

    private transient double globalSpawnRate = 0;

    public void loadGlobalStats() {
        for (BiomeType biome : this.list()) {
            globalSpawnRate += biome.getSpawner().attributesTopLevel().getSpawnRate();
        }
    }

    public double getGlobalSpawnRate() {
        return globalSpawnRate;
    }

    public List<SpawnSelectorGrouping> listSelectors() {
        return list().stream().map(BiomeType::getSpawnerTags).toList();
    }
}
