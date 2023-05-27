package voltskiya.mob.system.base.spawner.modifier.flying;

import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifier;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class FlyingModifier extends SpawningModifier<FlyingModifierConfig> {

    public FlyingModifier(FlyingModifierConfig config, SpawningContext context,
        SpawnerSummonResult result) {
        super(config, context, result);
    }

    @Override
    public void preModifyEntity() {
        System.err.println("premodified! :D");
        context.setLocation(config.getNewLocation());
    }

    @Override
    public void modifyEntity() {

    }
}
