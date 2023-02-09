package voltskiya.mob.system.base.storage.world;

import io.ebean.DB;

public class WorldStorage {

    public static double hitRatio(WorldUUID worldId) {
        StoredWorld world = getWorld(worldId);
        long total = world.spawnSuccessCount + world.spawnFailCount;
        if (total == 0) return 0;
        return (double) world.spawnSuccessCount / total;
    }

    private static StoredWorld getWorld(WorldUUID worldId) {
        StoredWorld world = DB.find(StoredWorld.class, worldId.worldId);
        if (world != null) return world;
        StoredWorld created = new StoredWorld(worldId.worldId, worldId.uuid);
        DB.getDefault().insert(created);
        return created;
    }
}
