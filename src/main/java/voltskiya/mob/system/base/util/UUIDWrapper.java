package voltskiya.mob.system.base.util;

import java.util.Objects;

public abstract class UUIDWrapper<Id, Mapped> {

    protected Id id;

    public UUIDWrapper(Id id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return isInstanceOf(obj) && obj instanceof UUIDWrapper<?, ?> other && Objects.equals(other.id, this.id);
    }

    public abstract boolean isInstanceOf(Object obj);

    public abstract Mapped mapped();

    public Id getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "id=" + id;
    }

}
