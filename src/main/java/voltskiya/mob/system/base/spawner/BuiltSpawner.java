package voltskiya.mob.system.base.spawner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.selector.SpawnSelector;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningAttributes;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningComputedAttributes;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.ShouldSpawningResult;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class BuiltSpawner {

    private final Set<SpawnSelector> fragments;
    private Set<Spawner> compiled;
    private Set<MobUUID> extendedByMob = null;
    private SpawningAttributes attributes = null;

    private BuiltSpawner(Set<SpawnSelector> fragments) {
        this.fragments = fragments;
    }

    public static BuiltSpawner fromBuilt(Collection<BuiltSpawner> builtFragments, SpawnSelector... other) {
        Set<SpawnSelector> fragments = new HashSet<>(List.of(other));
        for (BuiltSpawner built : builtFragments) fragments.addAll(built.fragments);
        return new BuiltSpawner(fragments);
    }

    public static BuiltSpawner fromOne(SpawnSelector selector) {
        return new BuiltSpawner(Collections.singleton(selector));
    }

    public void init() {
        this.compiled = fragments.stream().map(SpawnSelector::getImplementation).collect(Collectors.toUnmodifiableSet());
        this.extendedByMob = new HashSet<>();
        for (SpawnSelector fragment : fragments) {
            extendedByMob.addAll(fragment.getExtendedByMob());
        }
    }

    public synchronized ShouldSpawningResult shouldSpawn(SpawningContext context) {
        ShouldSpawningResult result = new ShouldSpawningResult();
        for (Spawner spawner : compiled) {
            if (spawner.isBreaksRule(context)) return ShouldSpawningResult.SHOULD_REMOVE;
            result.delayUntil(spawner.spawnDelay(context));
        }
        return result;
    }

    public synchronized SpawnerSummonResult spawnMob(SpawningContext context) {
        SpawnerSummonResult result = new SpawnerSummonResult(context);
        for (Spawner spawner : compiled)
            spawner.preModify(context, result);
        for (Spawner spawner : compiled)
            spawner.modify(context, result);
        return result;
    }

    public synchronized SpawningComputedAttributes attributesTopLevel() {
        return attributes(SpawningAttributes.empty()).getComputed();
    }

    public synchronized SpawningAttributes attributes(SpawningAttributes original) {
        if (attributes != null) {
            original.join(attributes);
            return original;
        }
        attributes = SpawningAttributes.empty();
        for (Spawner spawner : compiled) {
            spawner.attributes(attributes);
        }
        return attributes;
    }

    public synchronized Set<MobUUID> getExtendedByMob() {
        return this.extendedByMob;
    }
}
