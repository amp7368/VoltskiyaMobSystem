package voltskiya.mob.system.base.spawner.rule.generic.block;

import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.rule.generic.GsonMapSpawningRule;

public class BlockRulesWhitelist extends BlockRules {

    public BlockRulesWhitelist() {
        super(GsonMapSpawningRule.BLOCK_WHITELIST.getTypeId());
    }

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        return !this.isBlockContained(context);
    }
}
