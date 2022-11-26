package voltskiya.mob.system.base.storage.mob;

import io.ebean.DB;
import io.ebean.Transaction;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.storage.mob.query.QStoredMob;
import voltskiya.mob.system.player.world.mob.MobWorldSpawning;

public class MobStorage {

    public static void spawnMobs(SpawnMobsRegion region) {
        List<StoredMob> result;
        try (Transaction transaction = DB.getDefault().beginTransaction()) {
            result = queryRegion(region, transaction).findList();
            queryRegion(region, transaction).delete();
            transaction.commit();
        }
        MobWorldSpawning.spawnMobs(result);
    }

    @NotNull
    private static QStoredMob queryRegion(SpawnMobsRegion region, Transaction transaction) {
        return new QStoredMob().usingTransaction(transaction).where().and().location.world.eq(region.getWorldId()).location.x.between(
                region.getLowerX(), region.getUpperX()).location.z.between(region.getLowerZ(), region.getUpperZ()).spawnDelay.eq(0)
            .endAnd();
    }

    public static void insertMobs(List<StoredMob> mobsToAddBack) {
        DB.getDefault().insertAll(mobsToAddBack);
    }

    public static int countMobs(short worldId) {
        return new QStoredMob().where().location.world.eq(worldId).findCount();
    }

}
