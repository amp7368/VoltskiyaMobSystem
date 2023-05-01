package voltskiya.mob.system.base.selector;

import java.util.UUID;
import voltskiya.mob.system.base.util.UUIDWrapper;
import voltskiya.mob.system.base.util.UUIDWrapperTypeAdapter;

public class SpawnSelectorUUID extends UUIDWrapper<UUID, SpawnSelector> {


    public SpawnSelectorUUID(UUID id) {
        super(id);
    }

    public static UUIDWrapperTypeAdapter<UUID, SpawnSelectorUUID> typeAdapter() {
        return new UUIDWrapperTypeAdapter<>(SpawnSelectorUUID::new, UUID.class);
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
