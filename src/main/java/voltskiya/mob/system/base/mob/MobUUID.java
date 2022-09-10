package voltskiya.mob.system.base.mob;

public class MobUUID {

    public short uuid;

    @Override
    public int hashCode() {
        return uuid;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MobUUID other && other.uuid == this.uuid;
    }
}
