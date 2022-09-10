package voltskiya.mob.system.spawning.modifier.entity;

import voltskiya.mob.system.spawning.context.SpawningContext;

@FunctionalInterface
public interface SpawningModifierFactory {

    SpawningModifier createModifier(SpawningContext context);
}
