package voltskiya.mob.system.spawning.rule.temporal;

import voltskiya.mob.system.spawning.context.SpawningContext;

public abstract class SpawningTemporalRule {

    public abstract long spawnDelay(SpawningContext context);
}
