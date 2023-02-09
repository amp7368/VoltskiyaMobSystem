package voltskiya.mob.system.base.storage.mob;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.base.storage.world.WorldAdapter;

@Embeddable
public class StoredLocation {

    private transient World bukkitWorld;
    @Column(precision = 3)
    private short world;
    @Column(precision = 28)
    private long x;
    @Column(precision = 28)
    private long y;
    @Column(precision = 28)
    private long z;
    @Column(precision = 16, scale = 16)
    private double xDecimal;
    @Column(precision = 16, scale = 16)
    private double yDecimal;
    @Column(precision = 16, scale = 16)
    private double zDecimal;
    @Column
    private float yaw;
    @Column
    private float pitch;

    public StoredLocation(Location location) {
        this.world = WorldAdapter.get().getWorld(location.getWorld()).worldId;

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
            this.bukkitWorld = WorldAdapter.get().getWorld(this.world).getBukkit();
        return this.bukkitWorld;
    }
}
