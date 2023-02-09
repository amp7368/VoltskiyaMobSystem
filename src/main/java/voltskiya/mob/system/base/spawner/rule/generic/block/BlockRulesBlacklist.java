package voltskiya.mob.system.base.spawner.rule.generic.block;

import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.rule.generic.GsonMapSpawningRule;

public class BlockRulesBlacklist extends BlockRules {

    public BlockRulesBlacklist() {
        super(GsonMapSpawningRule.BLOCK_BLACKLIST.getTypeId());
    }

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        return this.isBlockContained(context);
    }
}
