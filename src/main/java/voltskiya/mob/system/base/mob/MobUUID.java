package voltskiya.mob.system.base.mob;

import voltskiya.mob.system.base.util.UUIDWrapper;

public class MobUUID extends UUIDWrapper<Integer, MobType> {


    public MobUUID(int id) {
        super(id);
    }

    @Override
    public boolean isInstanceOf(Object obj) {
        return obj instanceof MobUUID;
    }

    @Override
    public MobType mapped() {
        return MobTypeDatabase.getMobType(this);
    }
}
