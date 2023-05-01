package voltskiya.mob.system.base.biome;

import java.util.UUID;
import voltskiya.mob.system.base.util.UUIDWrapper;
import voltskiya.mob.system.base.util.UUIDWrapperTypeAdapter;

public class BiomeUUID extends UUIDWrapper<UUID, BiomeType> {

    public BiomeUUID(UUID uuid) {
        super(uuid);
    }

    public static BiomeUUID random() {
        return new BiomeUUID(UUID.randomUUID());
    }

    public static UUIDWrapperTypeAdapter<UUID, BiomeUUID> typeAdapter() {
        return new UUIDWrapperTypeAdapter<>(BiomeUUID::new, UUID.class);
    }

    @Override
    public boolean isInstanceOf(Object obj) {
        return obj instanceof BiomeUUID;
    }

    @Override
    public BiomeType mapped() {
        return BiomeDatabases.getBiomeType().get(this);
    }
}
