package voltskiya.mob.system.player.world.watch;

import com.voltskiya.lib.timings.scheduler.CancellingAsyncTask;
import com.voltskiya.lib.timings.scheduler.VoltTask;
import org.bukkit.entity.Player;
import voltskiya.mob.system.player.ModulePlayer;

public interface WatchHasPlayer {

    Player getPlayer();

    default void queue(Runnable task) {
        CancellingAsyncTask voltTask = VoltTask.cancelingAsyncTask(task);
        voltTask.start(ModulePlayer.get().getTaskManager());
    }
}
