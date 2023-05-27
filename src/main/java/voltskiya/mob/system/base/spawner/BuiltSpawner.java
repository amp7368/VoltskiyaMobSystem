package voltskiya.mob.system.base.spawner;

import java.util.Comparator;
import java.util.Set;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningComputedAttributes;

public class BuiltSpawner {

    private final Set<LeafSpawner> leaves;

    public BuiltSpawner(Set<LeafSpawner> leaves) {
        this.leaves = leaves;
    }

    public Set<LeafSpawner> getLeaves() {
        return leaves;
    }

    public double getMaxSpawnRate() {
        return leaves.stream().map(LeafSpawner::attributesTopLevel)
            .map(SpawningComputedAttributes::getSpawnRate)
            .max(Comparator.comparingDouble(s -> s)).orElse(1d);
    }
}
