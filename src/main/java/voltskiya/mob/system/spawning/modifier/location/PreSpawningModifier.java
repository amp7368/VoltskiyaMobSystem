package voltskiya.mob.system.spawning.modifier.location;

import voltskiya.mob.system.spawning.context.SpawningContext;
import voltskiya.mob.system.spawning.world.mob.SpawnerSummonResult;

public abstract class PreSpawningModifier {

    private SpawningContext context;

    public PreSpawningModifier(SpawningContext context) {
        this.context = context;
    }

    public void preModify(SpawnerSummonResult result) {

    }
}
