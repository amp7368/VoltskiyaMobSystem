package voltskiya.mob.system.base.spawner.rule.generic;

import apple.utilities.gson.adapter.GsonEnumTypeAdapter;
import apple.utilities.gson.adapter.GsonEnumTypeHolder;
import apple.utilities.json.gson.GsonBuilderDynamic;
import voltskiya.mob.system.base.spawner.rule.generic.block.BlockRulesBlacklist;
import voltskiya.mob.system.base.spawner.rule.generic.block.BlockRulesWhitelist;
import voltskiya.mob.system.base.spawner.rule.generic.elevation.ElevationRules;
import voltskiya.mob.system.base.spawner.rule.generic.flying.FlyingRules;

public enum GsonMapSpawningRule implements GsonEnumTypeHolder<SpawningRule> {
    BLOCK_BLACKLIST("block_blacklist", BlockRulesBlacklist.class),
    BLOCK_WHITELIST("block_whitelist", BlockRulesWhitelist.class),
    ELEVATION("elevation", ElevationRules.class),
    FLYING("flying", FlyingRules.class);

    private final String typeId;
    private final Class<? extends SpawningRule> type;

    GsonMapSpawningRule(String typeId, Class<? extends SpawningRule> type) {
        this.typeId = typeId;
        this.type = type;
    }

    public static void gson(GsonBuilderDynamic gson) {
        GsonEnumTypeAdapter.register(values(), gson, SpawningRule.class);
    }

    @Override
    public String getTypeId() {
        return typeId;
    }

    @Override
    public Class<? extends SpawningRule> getTypeClass() {
        return type;
    }
}
