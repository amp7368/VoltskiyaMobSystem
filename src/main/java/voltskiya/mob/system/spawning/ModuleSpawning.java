package voltskiya.mob.system.spawning;

import apple.lib.pmc.PluginModule;
import voltskiya.mob.system.spawning.world.watch.WatchPlayer;
import voltskiya.mob.system.spawning.world.watch.WatchPlayerListener;

public class ModuleSpawning extends PluginModule {

    private static ModuleSpawning instance;

    public ModuleSpawning() {
        instance = this;
    }

    public static ModuleSpawning get() {
        return instance;
    }

    @Override
    public void enable() {
        new WatchPlayerListener();
        WatchPlayer.load();
    }

    @Override
    public String getName() {
        return "Spawning";
    }
}
