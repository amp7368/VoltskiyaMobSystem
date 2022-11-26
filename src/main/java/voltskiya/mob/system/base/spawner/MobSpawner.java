package voltskiya.mob.system.base.spawner;

import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.ShouldSpawningResult;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class MobSpawner {

    private final MobSpawnerFragment[] fragments;

    public MobSpawner(MobSpawnerFragment... fragments) {
        this.fragments = fragments;
    }

    public ShouldSpawningResult shouldSpawn(SpawningContext context) {
        ShouldSpawningResult result = new ShouldSpawningResult();
        for (MobSpawnerFragment spawner : fragments) {
            if (spawner == null || spawner.isBreaksRule(context)) return ShouldSpawningResult.SHOULD_REMOVE;
            result.delayUntil(spawner.spawnDelay(context));
        }
        return result;
    }

    public SpawnerSummonResult spawnMob(SpawningContext context) {
        SpawnerSummonResult result = new SpawnerSummonResult(context);
        for (MobSpawnerFragment spawner : fragments)
            spawner.preModify(context, result);
        for (MobSpawnerFragment spawner : fragments)
            spawner.modify(context, result);
        return result;
    }
}
