package voltskiya.mob.system.spawn;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.spawn.config.RegenConfig;
import voltskiya.mob.system.spawn.task.WorldRegenDaemon;

public class OnlinePlayersListener implements Listener {

    public OnlinePlayersListener() {
        VoltskiyaPlugin.get().registerEvents(this);
        if (count() <= maxPlayers()) WorldRegenDaemon.get().start();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void join(PlayerJoinEvent event) {
        if (count() + 1 > maxPlayers())
            WorldRegenDaemon.get().stop();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void leave(PlayerQuitEvent event) {
        if (count() - 1 <= maxPlayers())
            WorldRegenDaemon.get().start();
    }

    private int maxPlayers() {
        return RegenConfig.get().maxPlayersWhileRunning;
    }

    private int count() {
        return Bukkit.getOnlinePlayers().size();
    }
}
