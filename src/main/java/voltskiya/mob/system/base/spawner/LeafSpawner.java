package voltskiya.mob.system.base.spawner;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.selector.ExtendsMob;
import voltskiya.mob.system.base.selector.SpawnSelector;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningAttributes;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningComputedAttributes;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.ShouldSpawningResult;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class LeafSpawner {

    private final Set<SpawnSelector> fragments;
    private Set<Spawner> compiled = null;
    private List<ExtendsMob> extendsMob = null;
    private SpawningAttributes attributes = null;

    public LeafSpawner(Set<SpawnSelector> fragments) {
        this.fragments = fragments;
    }

    public static LeafSpawner fromBuilt(Collection<LeafSpawner> builtFragments, SpawnSelector... other) {
        Set<SpawnSelector> fragments = new HashSet<>(List.of(other));
        for (LeafSpawner built : builtFragments) fragments.addAll(built.fragments);
        return new LeafSpawner(fragments);
    }

    private synchronized Set<Spawner> compiledSpawners() {
        if (this.compiled != null) return this.compiled;
        return this.compiled = fragments.stream()
            .map(SpawnSelector::getImplementation)
            .collect(Collectors.toUnmodifiableSet());
    }

    public ShouldSpawningResult shouldSpawn(SpawningContext context) {
        ShouldSpawningResult result = new ShouldSpawningResult();
        for (Spawner spawner : compiledSpawners()) {
            if (spawner.isBreaksRule(context)) return ShouldSpawningResult.SHOULD_REMOVE;
            result.delayUntil(spawner.spawnDelay(context));
        }
        return result;
    }

    public SpawnerSummonResult prepare(SpawningContext context, boolean skipModifications) {
        SpawnerSummonResult result = new SpawnerSummonResult(context);
        if (skipModifications) return result;
        for (Spawner spawner : compiledSpawners())
            spawner.prepareModifiers(context, result);
        context.applyModifications(result);
        return result;
    }

    public SpawningComputedAttributes attributesTopLevel() {
        return attributes(SpawningAttributes.empty()).getComputed();
    }

    public SpawningAttributes attributes(SpawningAttributes original) {
        if (attributes != null) {
            return original.join(attributes);
        }
        attributes = SpawningAttributes.empty();
        for (Spawner spawner : compiledSpawners()) {
            spawner.attributes(attributes);
        }
        return original.join(attributes);
    }

    public synchronized List<ExtendsMob> getExtendsMob() {
        if (this.extendsMob != null) return this.extendsMob;
        return this.extendsMob = fragments.stream()
            .flatMap(SpawnSelector::getExtendsMobStream)
            .toList();
    }

    public boolean hasMob(MobUUID id) {
        for (ExtendsMob mob : this.getExtendsMob()) {
            if (mob.mob.equals(id)) return true;
        }
        return false;
    }
}
