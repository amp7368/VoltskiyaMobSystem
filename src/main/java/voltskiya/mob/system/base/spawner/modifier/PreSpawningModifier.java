package voltskiya.mob.system.base.spawner.modifier;

import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public abstract class PreSpawningModifier {

    private SpawningContext context;

    public PreSpawningModifier(SpawningContext context) {
        this.context = context;
    }

    public void preModify(SpawnerSummonResult result) {

    }
}
