package voltskiya.mob.system.base.spawner.modifier;

import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public abstract class SpawningModifierFactory {

    private final String typeId;

    protected SpawningModifierFactory(String typeId) {
        this.typeId = typeId;
    }

    public abstract SpawningModifier<?> createModifier(SpawningContext context, SpawnerSummonResult result);
}
