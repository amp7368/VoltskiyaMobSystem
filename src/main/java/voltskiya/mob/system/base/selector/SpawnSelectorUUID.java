package voltskiya.mob.system.base.selector;

import voltskiya.mob.system.base.util.UUIDWrapper;

public class SpawnSelectorUUID extends UUIDWrapper<Integer, SpawnSelector> {


    public SpawnSelectorUUID(int id) {
        super(id);
    }

    @Override
    public SpawnSelector mapped() {
        return SpawnSelectorDatabase.get(this);
    }

    @Override
    public boolean isInstanceOf(Object obj) {
        return obj instanceof SpawnSelectorUUID;
    }

}
