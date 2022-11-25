package voltskiya.mob.system.spawn.spawner.rule.temporal;

import voltskiya.mob.system.spawn.spawner.context.SpawningContext;

public abstract class SpawningTemporalRule {

    public abstract long spawnDelay(SpawningContext context);
}
