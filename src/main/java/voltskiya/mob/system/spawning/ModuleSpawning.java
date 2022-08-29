package voltskiya.mob.system.spawning;

import apple.lib.pmc.PluginModule;

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

    }

    @Override
    public String getName() {
        return "Spawning";
    }
}
