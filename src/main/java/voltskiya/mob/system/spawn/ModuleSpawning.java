package voltskiya.mob.system.spawn;

import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.configs.factory.AppleConfigLike;
import java.util.List;
import voltskiya.mob.system.player.world.watch.WatchPlayerConfig;

public class ModuleSpawning extends AbstractModule {

    private static ModuleSpawning instance;

    public ModuleSpawning() {
        instance = this;
    }

    public static ModuleSpawning get() {
        return instance;
    }

    @Override
    public void enable() {
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(WatchPlayerConfig.class, "WatchPlayerConfig"));
    }

    @Override
    public String getName() {
        return "Spawning";
    }
}
