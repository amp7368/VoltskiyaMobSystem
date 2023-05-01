package voltskiya.mob.system.spawn.task;

import com.voltskiya.lib.timings.scheduler.VoltTask;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.spawn.config.RegenConfig;

public class WorldRegenDaemon {

    private static WorldRegenDaemon instance;
    private int serviceSize = 0;
    private BukkitTask runningScheduler;

    public WorldRegenDaemon() {
        instance = this;
    }

    public static WorldRegenDaemon get() {
        return instance;
    }

    private void scheduleTask() {
        if (this.incrementTask()) {
            WorldRegenTask task = new WorldRegenTask(this::decrementTask);
            VoltTask.cancelingAsyncTask(task).start(ModuleSpawning.get().getTaskManager());
        }
    }

    private void decrementTask() {
        synchronized (this) {
            serviceSize--;
        }
    }

    private boolean incrementTask() {
        synchronized (this) {
            if (serviceSize >= RegenConfig.get().maxThreadPool)
                return false;
            serviceSize++;
            return true;
        }
    }


    public void start() {
        synchronized (this) {
            scheduleThisScheduler();
        }
    }

    public void stop() {
        synchronized (this) {
            if (this.runningScheduler != null) {
                this.runningScheduler.cancel();
                this.runningScheduler = null;
            }
        }
    }

    private void scheduleThisScheduler() {
        synchronized (this) {
            if (this.runningScheduler != null) return;
            runningScheduler = Bukkit.getScheduler().runTaskTimerAsynchronously(VoltskiyaPlugin.get(), this::scheduleTask, 0, 1);
        }
    }
}
