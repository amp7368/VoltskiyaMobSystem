package voltskiya.mob.system.base.selector;

import java.util.UUID;
import voltskiya.mob.system.base.util.UUIDWrapper;

public class SpawnSelectorUUID extends UUIDWrapper<UUID, SpawnSelector> {


    public SpawnSelectorUUID(UUID id) {
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
