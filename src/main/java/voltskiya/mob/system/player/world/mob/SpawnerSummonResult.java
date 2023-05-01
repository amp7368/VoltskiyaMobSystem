package voltskiya.mob.system.player.world.mob;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifier;

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

    public void sort(ToIntFunction<SpawningModifier<?>> priorityExtractor) {
        modifiers.sort(Comparator.comparingInt(priorityExtractor));
    }

    public void spawn(@NotNull MobType mobType) {
        this.mobType = mobType;
        this.sort(modifier -> modifier.getPreModifyPriority().value());
        this.preModify();
        this.sort(modifier -> modifier.getModifyPriority().value());
        VoltskiyaPlugin.get()
            .scheduleSyncDelayedTask(() -> mobType.getEntity().spawn(this.location, this::modifyEntity));
    }

    private void preModify() {
        modifiers.forEach(SpawningModifier::preModifyEntity);
    }

    public void modifyEntity(Entity entity) {
        this.mobType.tagMob(entity);
        this.spawned = entity;
        this.modifiers.forEach(SpawningModifier::modifyEntity);
    }

    public Entity getSpawned() {
        return spawned;
    }
}
