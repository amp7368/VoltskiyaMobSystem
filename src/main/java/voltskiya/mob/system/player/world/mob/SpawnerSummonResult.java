package voltskiya.mob.system.player.world.mob;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifier;
import voltskiya.mob.system.base.spawner.modifier.group.GroupModifier;

public class SpawnerSummonResult {

    private final Location location;
    private final List<SpawningModifier<?>> modifiers = new ArrayList<>();
    private Entity spawned;
    private MobType mobType;

    public SpawnerSummonResult(@NotNull SpawningContext context) {
        this.location = context.location();
    }

    public void addModifier(SpawningModifier<?> modifier) {
        this.modifiers.add(modifier);
    }

    private void sort(ToIntFunction<SpawningModifier<?>> priorityExtractor) {
        modifiers.sort(Comparator.comparingInt(priorityExtractor));
    }

    public void spawn(@NotNull MobType mobType, Predicate<SpawningModifier<?>> filterModifications) {
        this.sort(modifier -> modifier.getPreModifyPriority().value());
        this.preModify(filterModifications);
        this.sort(modifier -> modifier.getModifyPriority().value());
        VoltskiyaPlugin.get()
            .scheduleSyncDelayedTask(() -> mobType.getEntity().spawn(this.location, e -> this.modifyEntity(e, filterModifications)));

    }


    public void spawn(@NotNull MobType mobType, boolean skipModifications) {
        this.mobType = mobType;
        if (skipModifications) {
            VoltskiyaPlugin.get()
                .scheduleSyncDelayedTask(() -> mobType.getEntity().spawn(this.location, entity -> this.mobType.tagMob(entity)));
            return;
        }
        this.sort(modifier -> modifier.getPreModifyPriority().value());
        this.preModify((m) -> true);
        this.sort(modifier -> modifier.getModifyPriority().value());
        VoltskiyaPlugin.get()
            .scheduleSyncDelayedTask(() -> mobType.getEntity().spawn(this.location, this::modifyEntity));
    }

    private void preModify(Predicate<SpawningModifier<?>> filterModifications) {
        modifiers.stream().filter(filterModifications).forEach(SpawningModifier::preModifyEntity);
    }

    private void modifyEntity(Entity entity) {
        modifyEntity(entity, m -> true);
    }

    private void modifyEntity(Entity entity, Predicate<SpawningModifier<?>> filterModifications) {
        this.mobType.tagMob(entity);
        this.spawned = entity;
        // get the first group we find rather than spawning double huge groups
        // please fix this // todo
        boolean hasGroupModifier = false;
        for (SpawningModifier<?> modifier : this.modifiers) {
            if (!filterModifications.test(modifier)) continue;
            if (modifier instanceof GroupModifier) {
                if (hasGroupModifier) continue;
                hasGroupModifier = true;
            }
            modifier.modifyEntity();
        }
    }

    public MobType getMobType() {
        return this.mobType;
    }
}
