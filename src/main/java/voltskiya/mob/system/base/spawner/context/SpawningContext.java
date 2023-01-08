package voltskiya.mob.system.base.spawner.context;

import org.bukkit.Location;
import org.bukkit.block.Block;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.base.spawner.rule.temporal.TimeOfDay;
import voltskiya.mob.system.base.storage.mob.DStoredMob;

public record SpawningContext(Location location, BiomeUUID biomeUUID, Block feetBlock, Block belowFeetBlock) {


    public static SpawningContext create(DStoredMob storedMob) {
        Location location = storedMob.getLocation();
        Block feetBlock = location.getBlock();
        Block belowFeetBlock = location.clone().subtract(0, 1, 0).getBlock();
        BiomeUUID biomeUUID = BiomeDatabases.getBiome(feetBlock.getBiome().getKey());
        return new SpawningContext(location, biomeUUID, feetBlock, belowFeetBlock);
    }

    public TimeOfDay timeOfDay() {
        return TimeOfDay.getTime(time());
    }

    public long time() {
        return this.location().getWorld().getTime();
    }

}
