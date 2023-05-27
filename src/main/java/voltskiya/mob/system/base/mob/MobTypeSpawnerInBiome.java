package voltskiya.mob.system.base.mob;

import voltskiya.mob.system.base.selector.ExtendsMob;
import voltskiya.mob.system.base.spawner.LeafSpawner;

public record MobTypeSpawnerInBiome(MobType mob, ExtendsMob extendsMob, LeafSpawner spawner) {

    public double getSpawnRate() {
        return extendsMob.spawnWeight * spawner.attributesTopLevel().getSpawnRate();
    }
}
