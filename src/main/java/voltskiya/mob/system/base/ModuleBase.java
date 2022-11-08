package voltskiya.mob.system.base;

import apple.lib.pmc.PluginModule;
import voltskiya.mob.system.base.biome.BiomeTypeDatabase;
import voltskiya.mob.system.base.world.WorldDatabase;

public class ModuleBase extends PluginModule {

    private static ModuleBase instance;

    public static ModuleBase get() {
        return instance;
    }

    public ModuleBase() {
        instance = this;
    }

    @Override
    public void enable() {
        VoltskiyaMobDatabase.load();
        WorldDatabase.load();
        BiomeTypeDatabase.load();
    }

    @Override
    public String getName() {
        return "Base";
    }
}
