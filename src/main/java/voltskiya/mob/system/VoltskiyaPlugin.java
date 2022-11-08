package voltskiya.mob.system;


import apple.lib.pmc.ApplePlugin;
import apple.lib.pmc.PluginModule;
import java.util.List;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.spawning.ModuleSpawning;
import voltskiya.mob.system.temperature.ModuleTemperature;

public class VoltskiyaPlugin extends ApplePlugin {

    public static final int BLOCKS_IN_CHUNK = 16;
    private static VoltskiyaPlugin instance;

    public VoltskiyaPlugin() {
        instance = this;
    }

    public static VoltskiyaPlugin get() {
        return instance;
    }

    @Override
    public List<PluginModule> getModules() {
        return List.of(new ModuleSpawning(), new ModuleBase(), new ModuleTemperature());
    }
}
