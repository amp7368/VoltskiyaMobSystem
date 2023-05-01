package voltskiya.mob.system.base.spawner.modifier;

import apple.utilities.gson.adapter.GsonEnumTypeAdapter;
import apple.utilities.gson.adapter.GsonEnumTypeHolder;
import apple.utilities.json.gson.GsonBuilderDynamic;
import voltskiya.mob.system.base.spawner.modifier.group.GroupModifierFactory;

public enum GsonMapSpawningModifier implements GsonEnumTypeHolder<SpawningModifierFactory> {
    GROUP("group", GroupModifierFactory.class);
    private final String typeId;
    private final Class<? extends SpawningModifierFactory> type;

    GsonMapSpawningModifier(String typeId, Class<? extends SpawningModifierFactory> type) {
        this.typeId = typeId;
        this.type = type;
    }

    public static void gson(GsonBuilderDynamic gson) {
        GsonEnumTypeAdapter.register(values(), gson, SpawningModifierFactory.class);
    }

    @Override
    public String getTypeId() {
        return typeId;
    }

    @Override
    public Class<? extends SpawningModifierFactory> getTypeClass() {
        return type;
    }
}
