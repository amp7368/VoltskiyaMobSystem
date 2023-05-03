package voltskiya.mob.system.player.world.watch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import voltskiya.mob.system.VoltskiyaPlugin;

public class WatchPlayer implements MobWatchPlayer, WatchHasPlayer {

    private static final Map<UUID, WatchPlayer> watches = new HashMap<>();

    private final Player player;

    private WatchPlayer(Player player) {
        this.player = player;
    }


    public static void load() {
        VoltskiyaPlugin.get().scheduleSyncDelayedTask(() -> Bukkit.getOnlinePlayers().forEach(WatchPlayer::putIfAbsent));
        Bukkit.getScheduler()
            .scheduleSyncRepeatingTask(VoltskiyaPlugin.get(), WatchPlayer::tickAll, 1, 10 * 20);

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

    private static boolean shouldTick(Player player) {
        GameMode gameMode = player.getGameMode();
        return gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE;
    }

    private void tick() {
        if (!player.isOnline()) {
            remove(player);
            return;
        }
        if (shouldTick(player))
            tickWatchPlayer();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

}
