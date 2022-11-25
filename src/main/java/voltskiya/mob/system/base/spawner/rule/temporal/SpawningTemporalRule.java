package voltskiya.mob.system.base.spawner.rule.temporal;

import voltskiya.mob.system.base.spawner.context.SpawningContext;

public abstract class SpawningTemporalRule {

    public abstract long spawnDelay(SpawningContext context);
}
