package voltskiya.mob.system.player.world.watch;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.storage.mob.MobStorage;
import voltskiya.mob.system.base.storage.mob.StoredMob;

public class WatchPlayerListener implements Listener {

    public WatchPlayerListener() {
        VoltskiyaPlugin.get().registerEvents(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        WatchPlayer.putIfAbsent(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        @NotNull Entity[] entities = event.getChunk().getEntities();
        List<StoredMob> mobsToUnload = new ArrayList<>();
        for (Entity entity : entities) {
            @Nullable MobUUID mobType = MobTypeDatabase.get().getMobUUID(entity);
            mobsToUnload.add(new StoredMob(mobType, entity.getLocation()));
        }
        MobStorage.insertMobs(mobsToUnload);
    }
}
