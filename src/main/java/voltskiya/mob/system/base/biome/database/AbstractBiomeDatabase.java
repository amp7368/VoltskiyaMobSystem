package voltskiya.mob.system.base.biome.database;

import org.bukkit.NamespacedKey;
import voltskiya.mob.system.base.AbstractDatabaseMap;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.biome.BiomeUUID;

public class AbstractBiomeDatabase<T> extends AbstractDatabaseMap<BiomeUUID, NamespacedKey, T> {

    @Override
    public BiomeUUID translateMinecraft(NamespacedKey minecraft) {
        return BiomeDatabases.getBiome(minecraft);
    }
}
