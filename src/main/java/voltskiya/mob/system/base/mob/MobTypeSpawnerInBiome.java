package voltskiya.mob.system.base.mob;

import voltskiya.mob.system.base.selector.ExtendsMob;
import voltskiya.mob.system.base.spawner.BuiltSpawner;

public record MobTypeSpawnerInBiome(MobType mob, ExtendsMob extendsMob, BuiltSpawner spawner) {

}
