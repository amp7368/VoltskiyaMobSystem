package voltskiya.mob.system.storage.mob;

import io.ebean.DB;
import io.ebean.Query;
import io.ebean.Transaction;
import java.util.List;
import voltskiya.mob.system.player.world.mob.MobWorldSpawning;
import voltskiya.mob.system.storage.mob.query.QDStoredMob;

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

    private static QDStoredMob queryRegion(SpawnMobsRegion region, Transaction transaction) {
        return new QDStoredMob()
            .where().and()
            .location.world.eq(region.getWorldId())
            .spawnDelay.eq(0)
            .location.x.between(region.getLowerX(), region.getUpperX())
            .location.z.between(region.getLowerZ(), region.getUpperZ());
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
