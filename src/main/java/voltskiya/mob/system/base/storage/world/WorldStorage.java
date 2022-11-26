package voltskiya.mob.system.base.storage.world;

import io.ebean.DB;
import java.util.Optional;
import voltskiya.mob.system.base.storage.world.query.QStoredWorld;

public class WorldStorage {

    public static double hitRatio(WorldUUID worldId) {
        StoredWorld world = getWorld(worldId);
        long total = world.spawnSuccessCount + world.spawnFailCount;
        if (total == 0) return 0;
        return (double) world.spawnSuccessCount / total;
    }

    private static StoredWorld getWorld(WorldUUID worldId) {
        Optional<StoredWorld> world = new QStoredWorld().where().id.eq(worldId.worldId).findOneOrEmpty();
        if (world.isEmpty()) {
            StoredWorld created = new StoredWorld(worldId.worldId, worldId.uuid);
            DB.getDefault().insert(created);
            return created;
        }
        return world.get();
    }
}
