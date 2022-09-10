package voltskiya.mob.system.spawning.modifier.location;

import voltskiya.mob.system.spawning.context.SpawningContext;

@FunctionalInterface
public interface PreSpawningModifierFactory {
    PreSpawningModifier createModifier(SpawningContext context);
}
