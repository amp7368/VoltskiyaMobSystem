package voltskiya.mob.system.storage.mob;

import org.bukkit.World;
import voltskiya.mob.system.VoltskiyaMobPlugin;
import voltskiya.mob.system.storage.world.WorldAdapter;

public class SpawnMobsRegion {


    private final int lowerX;
    private final int upperX;
    private final int lowerZ;
    private final int upperZ;
    private final short worldId;

    public SpawnMobsRegion(int centerChunkX, int centerChunkZ, int radius, World world) {
        this.lowerX = (centerChunkX - radius) * VoltskiyaMobPlugin.BLOCKS_IN_CHUNK;
        this.upperX = (centerChunkX + radius) * VoltskiyaMobPlugin.BLOCKS_IN_CHUNK;
        this.lowerZ = (centerChunkZ - radius) * VoltskiyaMobPlugin.BLOCKS_IN_CHUNK;
        this.upperZ = (centerChunkZ + radius) * VoltskiyaMobPlugin.BLOCKS_IN_CHUNK;
        this.worldId = WorldAdapter.get().getWorld(world).worldId;

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
