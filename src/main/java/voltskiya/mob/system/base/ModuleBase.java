package voltskiya.mob.system.base;

import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.configs.factory.AppleConfigLike;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.selector.SpawnSelector;
import voltskiya.mob.system.base.selector.SpawnSelectorDatabase;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;
import voltskiya.mob.system.base.util.MaterialDatabase;

public class ModuleBase extends AbstractModule {

    private static ModuleBase instance;

    public ModuleBase() {
        instance = this;
    }

    public static ModuleBase get() {
        return instance;
    }

    private static void initSpawnerTags() {
        List<SpawnSelectorGrouping> mobs = MobTypeDatabase.listSelectors();
        List<SpawnSelectorGrouping> biomes = BiomeDatabases.getBiomeType().listSelectors();
        Collection<SpawnSelector> selectors = SpawnSelectorDatabase.listSelectors();
        SpawnSelector.initAll(Stream.concat(mobs.stream(), biomes.stream()).toList(), selectors);
    }


    @Override
    public void enable() {
        SpawnSelectorDatabase.load();
        BiomeDatabases.load();
        MobTypeDatabase.load();

        initSpawnerTags();
        MobTypeDatabase.init();

        BiomeDatabases.getBiomeType().loadGlobalStats();
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
