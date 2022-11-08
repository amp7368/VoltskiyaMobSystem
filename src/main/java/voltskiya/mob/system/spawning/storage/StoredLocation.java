package voltskiya.mob.system.spawning.storage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import jline.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import voltskiya.mob.system.base.world.WorldDatabase;

@Embeddable
public class StoredLocation {

    private transient World bukkitWorld;
    @Column(precision = 3)
    private short world;
    @Column(precision = 28)
    private double x;
    @Column(precision = 28)
    private double y;
    @Column(precision = 28)
    private double z;
    @Column(precision = 16, scale = 16)
    private double xDecimal;
    @Column(precision = 16, scale = 16)
    private double yDecimal;
    @Column(precision = 16, scale = 16)
    private double zDecimal;
    private float yaw;
    private float pitch;

    public StoredLocation(Location location) {
        this.world = WorldDatabase.get().getWorld(location.getWorld());

        double x = location.getX();
        this.x = (int) x;
        this.xDecimal = x - this.x;
        double y = location.getY();
        this.y = (int) y;
        this.yDecimal = y - this.y;
        double z = location.getZ();
        this.z = (int) z;
        this.zDecimal = z - this.z;

        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public StoredLocation() {
    }

    @Nullable
    public Location toLocation() {
        World world = bukkitWorld();
        if (world == null)
            return null;
        return new Location(world, x(), y(), z(), this.yaw, this.pitch);
    }

    private double x() {
        return this.x + this.xDecimal;
    }

    private double y() {
        return this.y + this.yDecimal;
    }

    private double z() {
        return this.z + this.zDecimal;
    }

    @Nullable
    private World bukkitWorld() {
        if (this.bukkitWorld == null)
            this.bukkitWorld = Bukkit.getWorld(WorldDatabase.get().getWorld(this.world));
        return this.bukkitWorld;
    }
}
