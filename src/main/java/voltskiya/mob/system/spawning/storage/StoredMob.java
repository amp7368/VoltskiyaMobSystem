package voltskiya.mob.system.spawning.storage;

import org.bukkit.Location;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.mob.MobUUID;

public class StoredMob {

    private MobUUID mobType;
    private Location location;
    private long spawnDelay;

    public StoredMob(MobUUID mobType, Location location) {
        this.mobType = mobType;
        this.location = location;
        this.spawnDelay = 0;
    }

    public StoredMob() {
    }

    public MobType getMobType() {
        return MobTypeDatabase.get().getMobType(this.mobType);
    }

    public Location getLocation() {
        return this.location;
    }

    public MobUUID getMobUUID() {
        return this.mobType;
    }

    public void setSpawnDelay(long spawnDelay) {
        this.spawnDelay = spawnDelay;
    }
}
