package voltskiya.mob.system.spawning.context;

import org.bukkit.Location;
import org.bukkit.block.Block;
import voltskiya.mob.system.base.biome.BiomeTypeDatabase;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.spawning.storage.StoredMob;

public record SpawningContext(Location location, BiomeUUID biomeUUID, Block feetBlock,
                              Block belowFeetBlock) {


    public static SpawningContext create(StoredMob storedMob) {
        Location location = storedMob.getLocation();
        Block feetBlock = location.getBlock();
        Block belowFeetBlock = location.clone().subtract(0, 1, 0).getBlock();
        BiomeUUID biomeUUID = BiomeTypeDatabase.getBiomeUUID(feetBlock.getBiome().getKey());
        return new SpawningContext(location, biomeUUID, feetBlock, belowFeetBlock);
    }
}
