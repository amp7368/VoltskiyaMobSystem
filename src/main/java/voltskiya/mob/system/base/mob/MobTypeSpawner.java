package voltskiya.mob.system.base.mob;

import voltskiya.mob.system.base.spawner.BuiltSpawner;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningComputedAttributes;

public record MobTypeSpawner(MobType mob, BuiltSpawner spawner) {

    public SpawningComputedAttributes attributesTopLevel() {
        return spawner.attributesTopLevel();
    }
}
