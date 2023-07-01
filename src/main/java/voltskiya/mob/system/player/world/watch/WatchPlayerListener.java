package voltskiya.mob.system.player.world.watch;

import apple.mc.utilities.data.serialize.EntitySerializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.VoltskiyaMobPlugin;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.storage.mob.MobStorage;
import voltskiya.mob.system.storage.mob.StoredLocation;
import voltskiya.mob.system.storage.mob.clone.DMobClone;
import voltskiya.mob.system.storage.mob.clone.DMobTag;
import voltskiya.mob.system.storage.mob.clone.MobCloneStorage;
import voltskiya.mob.system.storage.mob.typed.DStoredMob;

public class WatchPlayerListener implements Listener {

    public WatchPlayerListener() {
        VoltskiyaMobPlugin.get().registerEvents(this);
        VoltskiyaMobPlugin.get().scheduleSyncDelayedTask(() -> {
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                for (World world : Bukkit.getWorlds()) {
                    unloadEntities(world.getEntities());
                }
            }
        });
    }

    private static void unloadEntities(List<Entity> worldEntities) {
        List<DStoredMob> mobsToUnload = new ArrayList<>();
        List<DMobClone> clonesToUnload = new ArrayList<>();
        for (Entity entity : worldEntities) {
            @Nullable MobUUID mobType = MobTypeDatabase.getMobUUID(entity);
            if (mobType != null) {
                mobsToUnload.add(new DStoredMob(mobType, entity.getLocation()).setIsFirstSpawn(false));
                entity.remove();
                continue;
            }
            if (!MobCloneStorage.hasCloneTag(entity)) {
                entity.getScoreboardTags();
                continue;
            }
            DMobClone mob = new DMobClone(new EntitySerializable(entity), new StoredLocation(entity.getLocation()));
            for (DMobTag cloneTag : MobCloneStorage.getCloneTags(entity))
                mob.addTag(cloneTag);
            entity.remove();
            clonesToUnload.add(mob);
        }
        boolean isEmpty = mobsToUnload.isEmpty() && clonesToUnload.isEmpty();
        if (!isEmpty) {
            Bukkit.getScheduler().runTaskAsynchronously(VoltskiyaMobPlugin.get(), () -> {
                MobStorage.insertMobs(mobsToUnload);
                MobCloneStorage.insertMobs(clonesToUnload);
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        WatchPlayer.putIfAbsent(event.getPlayer());
    }

    @EventHandler
    public void onChunkUnload(EntitiesUnloadEvent event) {
        unloadEntities(event.getEntities());
    }
}
