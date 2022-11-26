package voltskiya.mob.system.spawn.task;

import java.util.concurrent.ExecutionException;
import org.bukkit.Bukkit;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.spawn.config.RegenConfig;

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
            try {
                this.worldRegen.run().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
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
