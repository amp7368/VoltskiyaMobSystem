package voltskiya.mob.system.spawn;

import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.configs.factory.AppleConfigLike;
import java.util.List;
import org.bukkit.Bukkit;
import voltskiya.mob.system.VoltskiyaMobPlugin;
import voltskiya.mob.system.spawn.config.RegenConfig;
import voltskiya.mob.system.spawn.config.RegenStatsMap;
import voltskiya.mob.system.spawn.task.WorldRegenDaemon;
import voltskiya.mob.system.storage.world.WorldAdapter;

public class ModuleSpawning extends AbstractModule {

    public static SpawningStats stats = new SpawningStats();
    private static ModuleSpawning instance;

    public ModuleSpawning() {
        instance = this;
    }

    public static ModuleSpawning get() {
        return instance;
    }

    @Override
    public void init() {
        WorldAdapter.load();
    }

    @Override
    public void enable() {
        RegenConfig.load();
        Bukkit.getScheduler().runTaskAsynchronously(VoltskiyaMobPlugin.get(), this::enableAsync);
    }

    private void enableAsync() {
        RegenStatsMap.load();
        new WorldRegenDaemon();
        new OnlinePlayersListener();
    }

    @Override
    public void onDisable() {
        WorldRegenDaemon.get().stop();
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of();
    }

    @Override
    public String getName() {
        return "Spawning";
    }
}
