package voltskiya.mob.system.base.biome.database;

import java.util.List;
import voltskiya.mob.system.base.biome.BiomeType;

public class BiomeTypeDatabase extends AbstractBiomeDatabase<BiomeType> {

    private double globalSpawnRate = 0;

    public void loadGlobalStats() {
        List<BiomeType> allBiomes = this.list();
        for (BiomeType biome : allBiomes) {
            globalSpawnRate += biome.getSpawner().attributesTopLevel().getSpawnRate();
            biome.loadGlobalStats();
        }
    }

    public double getGlobalSpawnRate() {
        return globalSpawnRate;
    }
}
