package voltskiya.mob.system.base.spawner.rule.generic.block;

import voltskiya.mob.system.base.spawner.context.SpawningContext;

public class BlockRulesBlacklist extends BlockRules {

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        return this.isBlockContained(context);
    }
}
