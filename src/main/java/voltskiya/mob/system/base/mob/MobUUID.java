package voltskiya.mob.system.base.mob;

import voltskiya.mob.system.base.util.UUIDWrapper;
import voltskiya.mob.system.base.util.UUIDWrapperTypeAdapter;

public class MobUUID extends UUIDWrapper<Integer, MobType> {


    public MobUUID(int id) {
        super(id);
    }

    public static UUIDWrapperTypeAdapter<Integer, MobUUID> typeAdapter() {
        return new UUIDWrapperTypeAdapter<>(MobUUID::new, Integer.class);
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
