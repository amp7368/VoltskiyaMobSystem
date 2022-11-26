package voltskiya.mob.system.spawn.config;

import java.util.Random;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
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
    private transient final Random random = new Random();

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

    private int yMin() {
        World world = getWorld().getBukkit();
        return world == null ? 0 : world.getMinHeight();
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

    @Nullable
    public Location randomLocation() {
        World bukkit = getWorld().getBukkit();
        if (bukkit == null) return null;
        int xRange = xRange();
        int yRange = yRange();
        int zRange = zRange();
        if (xRange == 0 || yRange == 0 || zRange == 0) return null;
        int x;
        int y;
        int z;
        synchronized (random) {
            x = random.nextInt(xRange) + xMin();
            y = random.nextInt(yRange) + yMin();
            z = random.nextInt(zRange) + zMin();
        }
        return new Location(bukkit, x, y, z);
    }
}
