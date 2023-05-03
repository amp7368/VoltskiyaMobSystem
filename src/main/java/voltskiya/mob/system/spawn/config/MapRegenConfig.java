package voltskiya.mob.system.spawn.config;

import apple.mc.utilities.item.material.MaterialUtils;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.spawn.task.WorldRegenTimings;
import voltskiya.mob.system.storage.world.WorldAdapter;
import voltskiya.mob.system.storage.world.WorldUUID;

public class MapRegenConfig {


    private static final byte RANDOM_LOCATION_HEIGHT = 100;
    private transient final Random random = new Random();
    public UUID world;
    public String worldName;
    //todo set this in-game through command
    public boolean mobSpawningIsOn = false;
    public int x1 = 0;
    public int x2 = 0;
    public int z1 = 0;
    public int z2 = 0;
    public double density = 20.0;
    private transient int xMin;
    private transient int xMax;
    private transient int yMin;
    private transient int yMax;
    private transient int zMin;
    private transient int zMax;
    private transient WorldUUID worldUUID;
    private transient int yMinStarting;

    public MapRegenConfig(World world) {
        this.world = world.getUID();
        this.worldName = world.getName();
    }

    public MapRegenConfig() {
    }

    public void init() {
        World bukkitWorld = getWorld().getBukkit();
        if (bukkitWorld == null) yMin = yMax = 0;
        else {
            yMin = bukkitWorld.getMinHeight();
            yMax = bukkitWorld.getMaxHeight();
        }
        xMin = Math.min(x1, x2);
        xMax = Math.max(x1, x2);
        zMin = Math.min(x1, x2);
        zMax = Math.max(x1, x2);
        yMinStarting = yMin + RANDOM_LOCATION_HEIGHT;
    }


    public int xRange() {
        return xMax - xMin;
    }

    public int zRange() {
        return zMax - zMin;
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
            save();
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
            x = random.nextInt(xRange) + xMin;
            y = random.nextInt(yRange) + yMin;
            z = random.nextInt(zRange) + zMin;
        }
        return new Location(bukkit, x, y, z);
    }

    public Location randomGroundLocation(WorldRegenTimings timings) {
        Location center = randomLocation();
        if (center == null) return null;
        timings.chunkLoad();
        @NotNull Chunk chunk = getWorld().getBukkit().getChunkAt(center);
        timings.chunkLoad();

        timings.chunkScan();
        for (byte xi = 0; xi < VoltskiyaPlugin.BLOCKS_IN_CHUNK; xi++) {
            for (byte zi = 0; zi < VoltskiyaPlugin.BLOCKS_IN_CHUNK; zi++) {
                boolean hitAir = false;
                for (int y = center.getBlockY(), max = Math.max(yMin, y - RANDOM_LOCATION_HEIGHT); y > max; y--) {
                    @NotNull Material material = chunk.getBlock(xi, y, zi).getType();
                    if (MaterialUtils.isTree(material)) {
                        // find air again
                        hitAir = false;
                    } else if (MaterialUtils.isWalkThroughable(material)) {
                        hitAir = true;
                    } else if (hitAir) {
                        center.setY(y);
                        return center.add(.5 + xi, 1, .5 + zi);
                    }
                }
            }
        }
        timings.chunkScan();
        return null;
    }
}
