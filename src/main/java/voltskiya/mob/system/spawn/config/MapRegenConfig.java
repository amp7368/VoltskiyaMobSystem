package voltskiya.mob.system.spawn.config;

import java.util.UUID;
import org.bukkit.World;
import voltskiya.mob.system.base.storage.world.WorldAdapter;
import voltskiya.mob.system.base.storage.world.WorldUUID;

public class MapRegenConfig {

    public UUID world;
    public String worldName;
    //todo set this in-game through command
    public boolean mobSpawningIsOn = false;
    public int x1 = 0;
    public int x2 = 0;
    public int z1 = 0;
    public int z2 = 0;
    public double density = 20.0;
    private transient WorldUUID worldUUID;

    public MapRegenConfig(World world) {
        this.world = world.getUID();
        this.worldName = world.getName();
    }

    public MapRegenConfig() {
    }

    public int xMax() {
        return Math.max(x1, x2);
    }

    public int xMin() {
        return Math.min(x1, x2);
    }

    public int zMax() {
        return Math.max(z1, z2);
    }

    public int zMin() {
        return Math.min(z1, z2);
    }

    public int xRange() {
        return xMax() - xMin();
    }

    public int zRange() {
        return zMax() - zMin();
    }

    public int yRange() {
        World world = getWorld().getBukkit();
        if (world == null)
            return 0;
        return world.getMaxHeight() - world.getMinHeight();
    }

    public WorldUUID getWorld() {
        if (this.worldUUID != null) return this.worldUUID;
        this.worldUUID = WorldAdapter.get().getWorld(this.world);
        World bukkit = worldUUID.getBukkit();
        if (bukkit == null) return worldUUID;
        if (!bukkit.getName().equals(this.worldName)) {
            this.worldName = bukkit.getName();
            this.save();
        }
        return worldUUID;
    }

    private void save() {
        RegenConfig.save();
    }
}
