package voltskiya.mob.system.spawning.rule.old;

import apple.utilities.gson.adapter.GsonEnumTypeAdapter;
import apple.utilities.gson.adapter.GsonEnumTypeHolder;
import apple.utilities.json.gson.GsonBuilderDynamic;
import voltskiya.mob.system.spawning.rule.old.block.BlockRules;
import voltskiya.mob.system.spawning.rule.old.block.BlockRulesBlacklist;
import voltskiya.mob.system.spawning.rule.old.block.BlockRulesWhitelist;

public enum BlockConditionsTypes implements GsonEnumTypeHolder<BlockRules> {
    WHITELIST("blockWhitelist", BlockRulesWhitelist.class),
    BLACKLIST("blockBlacklist", BlockRulesBlacklist.class);

    private final String typeId;
    private final Class<? extends BlockRules> typeClass;

    BlockConditionsTypes(String typeId, Class<? extends BlockRules> typeClass) {
        this.typeId = typeId;
        this.typeClass = typeClass;
    }

    public static GsonBuilderDynamic register(GsonBuilderDynamic gson) {
        return GsonEnumTypeAdapter.register(values(), gson, BlockRules.class);
    }

    @Override
    public String getTypeId() {
        return this.typeId;
    }

    @Override
    public Class<? extends BlockRules> getTypeClass() {
        return typeClass;
    }
}
