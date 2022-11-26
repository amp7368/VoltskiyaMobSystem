package voltskiya.mob.system.spawn.task;

import org.bukkit.Bukkit;
import voltskiya.mob.system.VoltskiyaPlugin;

public class WorldRegenDaemon {

    private static WorldRegenDaemon instance;

    private boolean isRunning = false;
    private boolean shouldRun = false;
    private final WorldRegen worldRegen = new WorldRegen();


    public WorldRegenDaemon() {
        instance = this;
    }

    public static WorldRegenDaemon get() {
        return instance;
    }

    public void run() {
        while (true) {
            synchronized (this) {
                isRunning = shouldRun;
                if (!isRunning)
                    return;
            }
            this.worldRegen.run();
            synchronized (this) {
                isRunning = shouldRun;
                if (!isRunning)
                    return;
            }
        }
    }


    public void start() {
        synchronized (this) {
            this.shouldRun = true;
            if (!isRunning) {
                Bukkit.getScheduler().runTaskAsynchronously(VoltskiyaPlugin.get(), this::run);
            }
        }
    }

    public void stop() {
        synchronized (this) {
            this.shouldRun = false;
        }
    }
}
