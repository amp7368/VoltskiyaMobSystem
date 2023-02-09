package voltskiya.mob.system.base.storage.mob;

import io.ebean.DB;
import io.ebean.Query;
import io.ebean.Transaction;
import java.util.List;
import voltskiya.mob.system.player.world.mob.MobWorldSpawning;

public class MobStorage {

    public static void spawnMobs(SpawnMobsRegion region) {
        List<DStoredMob> result;
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            result = queryRegion(region, transaction).findList();
            queryRegion(region, transaction).delete();
            transaction.commit();
        }
        MobWorldSpawning.spawnMobs(result);
    }

    private static Query<DStoredMob> queryRegion(SpawnMobsRegion region, Transaction transaction) {
        String queryString = """
            where
            location_world = :world and
            spawnDelay = 0 and
            location_x between :x1 and :x2 and
            location_z between :z1 and :z2
            """;
        Query<DStoredMob> query = DB.createQuery(DStoredMob.class, queryString);
        query.usingTransaction(transaction);
        query.setParameters(region.getWorldId(), region.getLowerX(), region.getUpperX(), region.getLowerZ(), region.getUpperZ());
        return query;
    }

    public static void insertMobs(List<DStoredMob> mobsToAddBack) {
        DB.getDefault().insertAll(mobsToAddBack);
    }

    public static int countMobs(short worldId) {
        String queryString = """
            where location_world = :world
            """;
        Query<DStoredMob> query = DB.createQuery(DStoredMob.class, queryString);
        query.setParameter("world", worldId);
        return query.findCount();
    }

}
