package voltskiya.mob.system.base.spawner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import voltskiya.mob.system.base.selector.ExtendsMob;
import voltskiya.mob.system.base.selector.SpawnSelector;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningAttributes;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningComputedAttributes;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.ShouldSpawningResult;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class BuiltSpawner {

    private final Set<SpawnSelector> fragments;
    private Set<Spawner> compiled = null;
    private List<ExtendsMob> extendsMob = null;
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

    private Set<Spawner> compiledSpawners() {
        if (this.compiled != null) return this.compiled;
        return this.compiled = fragments.stream()
            .map(SpawnSelector::getImplementation)
            .collect(Collectors.toUnmodifiableSet());
    }

    public synchronized ShouldSpawningResult shouldSpawn(SpawningContext context) {
        ShouldSpawningResult result = new ShouldSpawningResult();
        for (Spawner spawner : this.compiledSpawners()) {
            if (spawner.isBreaksRule(context)) return ShouldSpawningResult.SHOULD_REMOVE;
            result.delayUntil(spawner.spawnDelay(context));
        }
        return result;
    }

    public synchronized SpawnerSummonResult prepare(SpawningContext context) {
        SpawnerSummonResult result = new SpawnerSummonResult(context);
        for (Spawner spawner : compiledSpawners())
            spawner.prepareModifiers(context, result);
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
        for (Spawner spawner : compiledSpawners()) {
            spawner.attributes(attributes);
        }
        return attributes;
    }

    public synchronized List<ExtendsMob> getExtendsMob() {
        if (this.extendsMob != null) return this.extendsMob;
        return this.extendsMob = fragments.stream()
            .flatMap(SpawnSelector::getExtendsMobStream)
            .toList();
    }
}
