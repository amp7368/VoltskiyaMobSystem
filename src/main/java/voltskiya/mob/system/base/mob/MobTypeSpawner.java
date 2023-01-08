package voltskiya.mob.system.base.mob;

import voltskiya.mob.system.base.spawner.BuiltSpawner;

public record MobTypeSpawner(MobType mob, BuiltSpawner spawner) {

    public double getSpawnRate() {
        return spawner.getSpawnRate();
    }
}
