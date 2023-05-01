package voltskiya.mob.system.player.world.watch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import voltskiya.mob.system.VoltskiyaPlugin;

public class WatchPlayer implements MobWatchPlayer, WatchHasPlayer {

    private static final Map<UUID, WatchPlayer> watches = new HashMap<>();

    private final Player player;
    private final ThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(5);

    private WatchPlayer(Player player) {
        this.player = player;
    }


    public static void load() {
        Bukkit.getScheduler()
            .scheduleSyncRepeatingTask(VoltskiyaPlugin.get(), WatchPlayer::tickAll, 1, 10 * 20);
        for (Player player : Bukkit.getOnlinePlayers()) {
            putIfAbsent(player);
        }
    }

    private static void tickAll() {
        List<WatchPlayer> watchPlayers;
        synchronized (watches) {
            watchPlayers = List.copyOf(watches.values());
        }
        watchPlayers.forEach(WatchPlayer::tick);
    }

    private static void remove(Player player) {
        synchronized (watches) {
            watches.remove(player.getUniqueId());
        }
    }

    public static void putIfAbsent(Player player) {
        synchronized (watches) {
            watches.computeIfAbsent(player.getUniqueId(), (key) -> new WatchPlayer(player));
        }
    }

    private void tick() {
        if (!player.isOnline()) {
            remove(player);
            return;
        }
        tickWatchPlayer();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ThreadPoolExecutor getThreadPool() {
        return this.threadPool;
    }
}
