package voltskiya.mob.system.storage.mob;

import io.ebean.DB;
import io.ebean.Transaction;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.bukkit.Bukkit;
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
            .spawnDelay.lt(Bukkit.getCurrentTick())
            .location.x.between(region.getLowerX(), region.getUpperX())
            .location.z.between(region.getLowerZ(), region.getUpperZ());
    }

    public static void insertMobs(List<DStoredMob> mobsToAddBack) {
        mobsToAddBack.forEach(mob -> {
            try {
                mob.save();
            } catch (EntityNotFoundException e) {
                mob.insert();
            }
        });
    }

    public static int countMobs(short worldId) {
        return new QDStoredMob()
            .where().and()
            .location.world.eq(worldId)
            .findCount();
    }

}
