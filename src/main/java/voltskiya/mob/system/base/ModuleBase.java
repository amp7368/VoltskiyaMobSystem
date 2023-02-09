package voltskiya.mob.system.base;

import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.configs.factory.AppleConfigLike;
import java.util.List;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.selector.SpawnSelectorDatabase;
import voltskiya.mob.system.base.selector.api.SpawnSelectorController;
import voltskiya.mob.system.base.storage.world.WorldAdapter;
import voltskiya.mob.system.base.util.MaterialDatabase;

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
        SpawnSelectorDatabase.load();
        WorldAdapter.load();
        BiomeDatabases.load();
        MobTypeDatabase.load();
        new SpawnSelectorController().register();
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(MaterialDatabase.class, "Materials"));
    }

    @Override
    public String getName() {
        return "Base";
    }
}
