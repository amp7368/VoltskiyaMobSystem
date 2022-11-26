package voltskiya.mob.system.base.mob;

public class MobUUID {

    public int id;

    public MobUUID(int id) {
        this.id = id;
    }

    public MobUUID() {
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MobUUID other && other.id == this.id;
    }
}
