package voltskiya.mob.system.base.mob;

public class MobUUID {

    public short typeUUID;

    public MobUUID(short typeUUID) {
        this.typeUUID = typeUUID;
    }

    @Override
    public int hashCode() {
        return typeUUID;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MobUUID other && other.typeUUID == this.typeUUID;
    }
}
