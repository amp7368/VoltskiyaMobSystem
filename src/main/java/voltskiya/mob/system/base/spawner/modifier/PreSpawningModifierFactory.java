package voltskiya.mob.system.base.spawner.modifier;

import voltskiya.mob.system.base.spawner.context.SpawningContext;

@FunctionalInterface
public interface PreSpawningModifierFactory {
    PreSpawningModifier createModifier(SpawningContext context);
}
