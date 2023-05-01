package voltskiya.mob.system.spawn.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import voltskiya.mob.system.VoltskiyaPlugin;

public class WorldRegenDaemon {

    private static final int MAX_SIZE = 5;
    private static WorldRegenDaemon instance;
    private final ExecutorService service = Executors.newFixedThreadPool(5);
    private int serviceSize = 0;
    private BukkitTask runningScheduler;

    public WorldRegenDaemon() {
        instance = this;
    }

    public static WorldRegenDaemon get() {
        return instance;
    }

    private void scheduleTask() {
        if (this.incrementTask())
            service.submit(new WorldRegenTask(this::decrementTask));
    }

    private void decrementTask() {
        synchronized (service) {
            serviceSize--;
        }
    }

    private boolean incrementTask() {
        synchronized (service) {
            if (serviceSize >= MAX_SIZE)
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
            runningScheduler = Bukkit.getScheduler().runTaskTimerAsynchronously(VoltskiyaPlugin.get(), this::scheduleTask, 0, 10);
        }
    }
}
