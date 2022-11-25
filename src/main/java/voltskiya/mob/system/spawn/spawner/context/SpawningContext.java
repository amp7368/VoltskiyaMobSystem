package voltskiya.mob.system.spawn.spawner.context;

import org.bukkit.Location;
import org.bukkit.block.Block;
import voltskiya.mob.system.base.biome.BiomeTypeDatabase;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.spawn.spawner.rule.temporal.TimeOfDay;
import voltskiya.mob.system.spawn.storage.StoredMob;

public record SpawningContext(Location location, BiomeUUID biomeUUID, Block feetBlock,
                              Block belowFeetBlock) {


    public static SpawningContext create(StoredMob storedMob) {
        Location location = storedMob.getLocation();
        Block feetBlock = location.getBlock();
        Block belowFeetBlock = location.clone().subtract(0, 1, 0).getBlock();
        BiomeUUID biomeUUID = BiomeTypeDatabase.getBiomeUUID(feetBlock.getBiome().getKey());
        return new SpawningContext(location, biomeUUID, feetBlock, belowFeetBlock);
    }

    public TimeOfDay timeOfDay() {
        return TimeOfDay.getTime(time());
    }

    public long time() {
        return this.location().getWorld().getTime();
    }

}
