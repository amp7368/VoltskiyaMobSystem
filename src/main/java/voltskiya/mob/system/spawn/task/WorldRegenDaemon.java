package voltskiya.mob.system.spawn.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import voltskiya.mob.system.VoltskiyaPlugin;

public class WorldRegenDaemon {

    private static WorldRegenDaemon instance;
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1, run -> {
        Thread thread = new Thread(run);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    });
    private BukkitTask runningScheduler;

    public WorldRegenDaemon() {
        instance = this;
    }

    public static WorldRegenDaemon get() {
        return instance;
    }

    private void scheduleTask() {
        service.submit(new WorldRegenTask()::run);
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
