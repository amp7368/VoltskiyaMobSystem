package voltskiya.mob.system.spawn.spawner.rule.generic;

import voltskiya.mob.system.spawn.spawner.context.SpawningContext;

public abstract class SpawningRule {

    public abstract boolean isBreaksRule(SpawningContext context);

}
