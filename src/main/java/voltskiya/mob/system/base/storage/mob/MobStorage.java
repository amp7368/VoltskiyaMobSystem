package voltskiya.mob.system.base.storage.mob;

import io.ebean.DB;
import io.ebean.Transaction;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.storage.mob.query.QDStoredMob;
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

    @NotNull
    private static QDStoredMob queryRegion(SpawnMobsRegion region, Transaction transaction) {
        return new QDStoredMob().usingTransaction(transaction).where().and().location.world.eq(region.getWorldId()).location.x.between(
                region.getLowerX(), region.getUpperX()).location.z.between(region.getLowerZ(), region.getUpperZ()).spawnDelay.eq(0)
            .endAnd();
    }

    public static void insertMobs(List<DStoredMob> mobsToAddBack) {
        DB.getDefault().insertAll(mobsToAddBack);
    }

    public static int countMobs(short worldId) {
        return new QDStoredMob().where().location.world.eq(worldId).findCount();
    }

}
