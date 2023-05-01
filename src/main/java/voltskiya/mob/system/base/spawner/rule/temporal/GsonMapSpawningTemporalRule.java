package voltskiya.mob.system.base.spawner.rule.temporal;

import apple.utilities.gson.adapter.GsonEnumTypeAdapter;
import apple.utilities.gson.adapter.GsonEnumTypeHolder;
import apple.utilities.json.gson.GsonBuilderDynamic;
import voltskiya.mob.system.base.spawner.rule.temporal.time.TimeRules;

public enum GsonMapSpawningTemporalRule implements GsonEnumTypeHolder<SpawningTemporalRule> {
    GROUP("time", TimeRules.class);
    private final String typeId;
    private final Class<? extends SpawningTemporalRule> type;

    GsonMapSpawningTemporalRule(String typeId, Class<? extends SpawningTemporalRule> type) {
        this.typeId = typeId;
        this.type = type;
    }

    public static void gson(GsonBuilderDynamic gson) {
        GsonEnumTypeAdapter.register(values(), gson, SpawningTemporalRule.class);
    }

    @Override
    public String getTypeId() {
        return typeId;
    }

    @Override
    public Class<? extends SpawningTemporalRule> getTypeClass() {
        return type;
    }

}
