package voltskiya.mob.system.spawning.conditions.block;

import apple.utilities.gson.adapter.GsonEnumTypeAdapter;
import apple.utilities.gson.adapter.GsonEnumTypeHolder;
import apple.utilities.json.gson.GsonBuilderDynamic;

public enum BlockConditionsTypes implements GsonEnumTypeHolder<BlockConditions> {
    WHITELIST("whitelist", BlockConditionsWhitelist.class),
    BLACKLIST("blacklist", BlockConditionsBlacklist.class);

    private final String typeId;
    private final Class<? extends BlockConditions> typeClass;

    BlockConditionsTypes(String typeId, Class<? extends BlockConditions> typeClass) {
        this.typeId = typeId;
        this.typeClass = typeClass;
    }

    public static GsonBuilderDynamic register(GsonBuilderDynamic gson) {
        return GsonEnumTypeAdapter.register(values(), gson, BlockConditions.class);
    }

    @Override
    public String getTypeId() {
        return this.typeId;
    }

    @Override
    public Class<? extends BlockConditions> getTypeClass() {
        return typeClass;
    }
}
