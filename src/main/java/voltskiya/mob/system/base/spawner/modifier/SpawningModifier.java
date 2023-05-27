package voltskiya.mob.system.base.spawner.modifier;

import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public abstract class SpawningModifier<Config> {

    protected Config config;
    protected SpawningContext context;
    protected SpawnerSummonResult result;

    public SpawningModifier(Config config, SpawningContext context, SpawnerSummonResult result) {
        this.config = config;
        this.context = context;
        this.result = result;
        this.result.addModifier(this);
    }


    public abstract void preModifyEntity();

    public SpawningModifierPriority getPreModifyPriority() {
        return SpawningModifierPriority.NORMAL;
    }

    public abstract void modifyEntity();

    public SpawningModifierPriority getModifyPriority() {
        return SpawningModifierPriority.NORMAL;
    }
}
