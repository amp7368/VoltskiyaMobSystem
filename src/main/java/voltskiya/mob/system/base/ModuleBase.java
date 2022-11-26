package voltskiya.mob.system.base;

import com.voltskiya.lib.AbstractModule;
import voltskiya.mob.system.base.biome.BiomeTypeDatabase;
import voltskiya.mob.system.base.storage.world.WorldAdapter;

public class ModuleBase extends AbstractModule {

    private static ModuleBase instance;

    public static ModuleBase get() {
        return instance;
    }

    public ModuleBase() {
        instance = this;
    }

    @Override
    public void init() {
        VoltskiyaDatabase.load();
    }

    @Override
    public void enable() {
        WorldAdapter.load();
        BiomeTypeDatabase.load();
    }

    @Override
    public String getName() {
        return "Base";
    }
}
