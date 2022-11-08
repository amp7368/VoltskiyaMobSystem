package voltskiya.mob.system.spawning.world.watch;

import java.util.concurrent.ThreadPoolExecutor;
import org.bukkit.entity.Player;

public interface WatchHasPlayer {

    Player getPlayer();

    ThreadPoolExecutor getThreadPool();

    default void queue(Runnable task) {
        this.getThreadPool().execute(task);
    }
}
