package voltskiya.mob.system.spawn.spawner.modifier;

import voltskiya.mob.system.spawn.spawner.context.SpawningContext;

@FunctionalInterface
public interface PreSpawningModifierFactory {
    PreSpawningModifier createModifier(SpawningContext context);
}
