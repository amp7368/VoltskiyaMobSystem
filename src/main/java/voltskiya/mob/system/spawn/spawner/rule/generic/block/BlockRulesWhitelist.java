package voltskiya.mob.system.spawn.spawner.rule.generic.block;

import voltskiya.mob.system.spawn.spawner.context.SpawningContext;

public class BlockRulesWhitelist extends BlockRules {

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        return !this.isBlockContained(context);
    }
}
