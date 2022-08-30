package voltskiya.mob.system.spawning.conditions;

import apple.utilities.json.gson.GsonBuilderDynamic;
import voltskiya.mob.system.spawning.conditions.block.BlockConditions;
import voltskiya.mob.system.spawning.conditions.block.BlockConditionsTypes;
import voltskiya.mob.system.spawning.conditions.elevation.ElevationConditions;
import voltskiya.mob.system.spawning.conditions.flying.FlyingConditions;
import voltskiya.mob.system.spawning.conditions.group.GroupConditions;
import voltskiya.mob.system.spawning.conditions.time.TimeConditions;
import voltskiya.mob.system.spawning.conditions.water.WaterConditions;

public class MobSpawnConditions {

    private ElevationConditions elevation;
    private WaterConditions inWater;
    private FlyingConditions isFlying;
    private TimeConditions timeConditions;
    private GroupConditions groupConditions;
    private BlockConditions blocks;

    public static GsonBuilderDynamic gson(GsonBuilderDynamic gson) {
        BlockConditionsTypes.register(gson);
        return gson;
    }

    public static GsonBuilderDynamic gson() {
        return gson(new GsonBuilderDynamic());
    }
}
