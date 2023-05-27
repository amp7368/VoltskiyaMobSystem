package voltskiya.mob.system.player.world.watch;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.storage.mob.DStoredMob;
import voltskiya.mob.system.storage.mob.MobStorage;

public class WatchPlayerListener implements Listener {

    public WatchPlayerListener() {
        VoltskiyaPlugin.get().registerEvents(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        WatchPlayer.putIfAbsent(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkUnload(EntitiesUnloadEvent event) {
        List<DStoredMob> mobsToUnload = new ArrayList<>();
        List<Entity> entitiesToUnload = new ArrayList<>();
        for (Entity entity : event.getEntities()) {
            @Nullable MobUUID mobType = MobTypeDatabase.getMobUUID(entity);
            if (mobType == null) continue;
            mobsToUnload.add(new DStoredMob(mobType, entity.getLocation()).setIsFirstSpawn(false));
            entitiesToUnload.add(entity);
        }
        if (!mobsToUnload.isEmpty())
            Bukkit.getScheduler().runTaskAsynchronously(VoltskiyaPlugin.get(), () -> {
                MobStorage.insertMobs(mobsToUnload);
                VoltskiyaPlugin.get().scheduleSyncDelayedTask(() -> {
                    entitiesToUnload.forEach(Entity::remove);
                });
            });
    }
}
