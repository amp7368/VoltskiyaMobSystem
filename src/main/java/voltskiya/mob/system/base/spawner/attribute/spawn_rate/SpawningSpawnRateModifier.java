package voltskiya.mob.system.base.spawner.attribute.spawn_rate;

import voltskiya.mob.system.base.spawner.attribute.base.SpawningAttributeModifier;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningComputedAttributes;

public class SpawningSpawnRateModifier extends SpawningAttributeModifier {

    public double rateModifier;

    @Override
    public void modify(SpawningComputedAttributes original) {
        original.updateSpawnRate(this::modify);
    }

    private double modify(double spawnRate) {
        return spawnRate * rateModifier;
    }
}
