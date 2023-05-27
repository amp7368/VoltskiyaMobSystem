package voltskiya.mob.system.base.spawner.attribute.base;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class SpawningComputedAttributes {

    private double spawnRate = 1;

    public SpawningComputedAttributes(List<SpawningAttributeModifier> modifiers) {
        modifiers.sort(Comparator.comparing(SpawningAttributeModifier::getPriority, Comparator.comparingInt(Enum::ordinal)));
        modifiers.forEach(m -> m.modify(this));
    }

    public double getSpawnRate() {
        return spawnRate;
    }

    public void updateSpawnRate(Function<Double, Double> update) {
        this.spawnRate = update.apply(this.spawnRate);
    }


}
