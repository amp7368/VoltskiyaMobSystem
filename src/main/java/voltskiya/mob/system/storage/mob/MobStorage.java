package voltskiya.mob.system.storage.mob;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import voltskiya.mob.system.VoltskiyaMobPlugin;
import voltskiya.mob.system.player.world.mob.MobWorldSpawning;
import voltskiya.mob.system.storage.mob.clone.DMobClone;
import voltskiya.mob.system.storage.mob.clone.query.QDMobClone;
import voltskiya.mob.system.storage.mob.typed.DStoredMob;
import voltskiya.mob.system.storage.mob.typed.query.QDStoredMob;
import voltskiya.mob.system.storage.world.WorldAdapter;

public class MobStorage {

    public static void resetMobSpawnDelay() {
        new QDStoredMob().asUpdate().set("spawn_delay", 0).update();
    }

    public static synchronized void spawnMobs(SpawnMobsRegion region) {
        Set<DStoredMob> typedMobs = new HashSet<>(queryTypedRegion(region).findList());
        typedMobs.removeIf(mob -> !mob.delete());
        Set<DMobClone> clonedMobs = new HashSet<>(queryClonedRegion(region).findList());
        clonedMobs.removeIf(mob -> !mob.delete());

        MobWorldSpawning.spawnTypedMobs(typedMobs);
        VoltskiyaMobPlugin.get().scheduleSyncDelayedTask(() -> clonedMobs.forEach(DMobClone::spawn));
    }

    private static QDMobClone queryClonedRegion(SpawnMobsRegion region) {
        return new QDMobClone()
            .where().and()
            .location.world.eq(region.getWorldId())
            .location.x.between(region.getLowerX(), region.getUpperX())
            .location.z.between(region.getLowerZ(), region.getUpperZ())
            .endAnd();
    }

    private static QDStoredMob queryTypedRegion(SpawnMobsRegion region) {
        return new QDStoredMob()
            .where().and()
            .location.world.eq(region.getWorldId())
            .spawnDelay.lt(Bukkit.getCurrentTick())
            .location.x.between(region.getLowerX(), region.getUpperX())
            .location.z.between(region.getLowerZ(), region.getUpperZ())
            .endAnd();
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

    public static int countMobs(Location center, int radius, int yRadius) {
        short worldId = WorldAdapter.get().getWorld(center.getWorld()).worldId;
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();
        return new QDStoredMob()
            .where().and()
            .location.world.eq(worldId)
            .location.y.between(y - yRadius, y + yRadius)
            .location.x.between(x - radius, x + radius)
            .location.z.between(z - radius, z + radius)
            .endAnd()
            .findCount();
    }
}
