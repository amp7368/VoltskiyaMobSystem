package voltskiya.mob.system.spawn.spawner.modifier;

import voltskiya.mob.system.spawn.spawner.context.SpawningContext;
import voltskiya.mob.system.spawn.world.mob.SpawnerSummonResult;

public abstract class PreSpawningModifier {

    private SpawningContext context;

    public PreSpawningModifier(SpawningContext context) {
        this.context = context;
    }

    public void preModify(SpawnerSummonResult result) {

    }
}
