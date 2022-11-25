package voltskiya.mob.system.spawn.storage;

import org.bukkit.World;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.world.WorldDatabase;

public class SpawnMobsRegion {


    private final int lowerX;
    private final int upperX;
    private final int lowerZ;
    private final int upperZ;
    private final short worldId;

    public SpawnMobsRegion(int centerChunkX, int centerChunkZ, int radius, World world) {
        this.lowerX = (centerChunkX - radius) * VoltskiyaPlugin.BLOCKS_IN_CHUNK;
        this.upperX = (centerChunkX + radius) * VoltskiyaPlugin.BLOCKS_IN_CHUNK;
        this.lowerZ = (centerChunkZ - radius) * VoltskiyaPlugin.BLOCKS_IN_CHUNK;
        this.upperZ = (centerChunkZ + radius) * VoltskiyaPlugin.BLOCKS_IN_CHUNK;
        this.worldId = WorldDatabase.get().getWorld(world);

    }

    public int getLowerX() {
        return lowerX;
    }

    public int getUpperX() {
        return upperX;
    }

    public int getLowerZ() {
        return lowerZ;
    }

    public int getUpperZ() {
        return upperZ;
    }

    public short getWorldId() {
        return worldId;
    }
}
