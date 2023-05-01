package voltskiya.mob.system.base.spawner.attribute;

import apple.utilities.gson.adapter.GsonEnumTypeAdapter;
import apple.utilities.gson.adapter.GsonEnumTypeHolder;
import apple.utilities.json.gson.GsonBuilderDynamic;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningAttributeModifier;
import voltskiya.mob.system.base.spawner.attribute.spawn_rate.SpawningSpawnRateModifier;

public enum GsonMapSpawningAttribute implements GsonEnumTypeHolder<SpawningAttributeModifier> {
    SPAWN_RATE("spawn_rate", SpawningSpawnRateModifier.class);

    private final String typeId;
    private final Class<? extends SpawningAttributeModifier> typeClass;

    GsonMapSpawningAttribute(String typeId, Class<? extends SpawningAttributeModifier> typeClass) {
        this.typeId = typeId;
        this.typeClass = typeClass;
    }

    public static void gson(GsonBuilderDynamic gson) {
        GsonEnumTypeAdapter.register(values(), gson, SpawningAttributeModifier.class);
    }

    @Override
    public String getTypeId() {
        return this.typeId;
    }

    @Override
    public Class<? extends SpawningAttributeModifier> getTypeClass() {
        return this.typeClass;
    }
}
