package voltskiya.mob.system.storage.mob;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.storage.BaseEntity;

@Entity
@Table(name = "stored_mob")
public class DStoredMob extends BaseEntity {

    @Id
    private long id;

    @Convert(converter = MobUUIDConverter.class)
    @Column(nullable = false)
    private MobUUID mobType;
    private transient MobType mobTypeMemory;

    @Column(nullable = false)
    @Embedded(prefix = "location_")
    private StoredLocation location;

    @Column(nullable = false)
    private long spawnDelay;

    @Column(nullable = false)
    private boolean isFirstSpawn = true;

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

    public boolean isFirstSpawn() {
        return isFirstSpawn;
    }

    public DStoredMob setIsFirstSpawn(boolean isFirstSpawn) {
        this.isFirstSpawn = isFirstSpawn;
        return this;
    }
}
