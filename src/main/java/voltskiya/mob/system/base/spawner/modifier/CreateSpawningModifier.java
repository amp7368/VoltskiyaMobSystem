package voltskiya.mob.system.base.spawner.modifier;

import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public interface CreateSpawningModifier {

    void createModifier(SpawningContext context, SpawnerSummonResult result);

}
