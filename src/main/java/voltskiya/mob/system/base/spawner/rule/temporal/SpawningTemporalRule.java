package voltskiya.mob.system.base.spawner.rule.temporal;

import voltskiya.mob.system.base.spawner.context.SpawningContext;

public abstract class SpawningTemporalRule {

    protected String typeId;

    public SpawningTemporalRule(String typeId) {
        this.typeId = typeId;
    }

    public abstract long spawnDelay(SpawningContext context);
}
