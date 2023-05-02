package voltskiya.mob.system.base.spawner.rule.generic.water;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.rule.generic.GsonMapSpawningRule;
import voltskiya.mob.system.base.spawner.rule.generic.SpawningRule;

public class WaterRule extends SpawningRule {

    protected WaterRuleOption allow;

    public WaterRule() {
        super(GsonMapSpawningRule.WATER.getTypeId());
    }

    private static boolean isInWater(Block block) {
        if (block.getType() == Material.WATER) return true;

        return (block.getBlockData() instanceof Waterlogged waterlogged) && waterlogged.isWaterlogged();
    }

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        boolean isInWater = isInWater(context.location().getBlock());
        return allow.isOnlyWater() == isInWater;
    }
}
