package voltskiya.mob.system.base.storage.mob;

import io.ebean.annotation.Identity;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.mob.MobUUID;

@Entity
public class DStoredMob {

    @Id
    @Identity
    private long id;

    @Convert(converter = MobUUIDConverter.class)
    @Column(nullable = false)
    private MobUUID mobType;
    private transient MobType mobTypeMemory;

    @Column(nullable = false)
    private StoredLocation location;

    @Column(nullable = false)
    private long spawnDelay;


    public DStoredMob(MobUUID mobType, @NotNull Location location) {
        this.mobType = mobType;
        this.location = new StoredLocation(location);
        this.spawnDelay = 0;
    }

    public DStoredMob() {
    }

    public MobType getMobType() {
        if (this.mobTypeMemory == null)
            this.mobTypeMemory = MobTypeDatabase.getMobType(this.mobType);
        return mobTypeMemory;
    }

    public Location getLocation() {
        return this.location.toLocation();
    }

    public MobUUID getMobUUID() {
        return this.mobType;
    }

    public void setSpawnDelay(long spawnDelay) {
        this.spawnDelay = spawnDelay;
    }
}
