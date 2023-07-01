package voltskiya.mob.system.spawn.config;

import apple.mc.utilities.item.material.MaterialUtils;
import apple.utilities.structures.Pair;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.VoltskiyaMobPlugin;
import voltskiya.mob.system.spawn.task.WorldRegenTimings;
import voltskiya.mob.system.storage.world.WorldAdapter;
import voltskiya.mob.system.storage.world.WorldUUID;

public class MapRegenConfig {


    private static final byte RANDOM_LOCATION_HEIGHT = 75;
    private transient final Random random = new SecureRandom();
    private transient final List<Pair<Byte, Byte>> coords = new ArrayList<>(
        VoltskiyaMobPlugin.BLOCKS_IN_CHUNK * VoltskiyaMobPlugin.BLOCKS_IN_CHUNK);
    public UUID world;
    public String worldName;
    //todo set this in-game through command
    public boolean mobSpawningIsOn = false;
    public int x1 = 0;
    public int x2 = 0;
    public int z1 = 0;
    public int z2 = 0;
    public double density = 50.0;
    private transient int xMin;
    private transient int xMax;
    private transient int yMin;
    private transient int yMax;
    private transient int zMin;
    private transient int zMax;
    private transient WorldUUID worldUUID;

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
        zMin = Math.min(z1, z2);
        zMax = Math.max(z1, z2);
        for (byte xi = 0; xi < VoltskiyaMobPlugin.BLOCKS_IN_CHUNK; xi++) {
            for (byte zi = 0; zi < VoltskiyaMobPlugin.BLOCKS_IN_CHUNK; zi++) {
                coords.add(new Pair<>(xi, zi));
            }
        }
    }


    public int xRange() {
        return xMax - xMin;
    }

    public int zRange() {
        return zMax - zMin;
    }


    public int yRange() {
        return yMax - yMin;
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
        int yRange = yRange() - RANDOM_LOCATION_HEIGHT;
        int zRange = zRange();
        if (xRange == 0 || yRange <= 0 || zRange == 0) return null;
        int x;
        int y;
        int z;
        synchronized (random) {
            x = random.nextInt(xRange) + xMin;
            y = random.nextInt(yRange) + yMin + RANDOM_LOCATION_HEIGHT;
            z = random.nextInt(zRange) + zMin;
        }
        return new Location(bukkit, x, y, z);
    }

    public Location randomGroundLocation(WorldRegenTimings timings) {
        Location center = randomLocation();
        if (center == null) return null;
        timings.chunkLoad();
        World world = getWorld().getBukkit();
        if (world == null) return null;
        @NotNull Chunk chunk = world.getChunkAt(center);
        timings.chunkLoad();
        timings.chunkScan();
        Collections.shuffle(this.coords, random);
        int localYMin = Math.max(yMin, center.getBlockY() - RANDOM_LOCATION_HEIGHT);
        for (Pair<Byte, Byte> coord : coords) {
            byte xi = coord.getKey();
            byte zi = coord.getValue();
            boolean hitAir = false;
            for (int y = center.getBlockY(); y > localYMin; y--) {
                @NotNull Material material = chunk.getBlock(xi, y, zi).getType();
                if (MaterialUtils.isTree(material)) {
                    // find air again
                    hitAir = false;
                } else if (MaterialUtils.isWalkThroughable(material)) {
                    hitAir = true;
                } else if (hitAir) {
                    return chunk.getBlock(xi, y, zi)
                        .getLocation()
                        .add(0.5, 1, 0.5);
                } else if (material == Material.WATER) {
                    hitAir = true;
                }
            }
        }
        timings.chunkScan();
        return null;
    }
}
