package voltskiya.mob.system.storage;

import com.voltskiya.lib.AbstractModule;
import voltskiya.mob.system.storage.mob.MobStorage;

public class ModuleStorage extends AbstractModule {

    private static ModuleStorage instance;

    public ModuleStorage() {
        instance = this;
    }

    public static ModuleStorage get() {
        return instance;
    }

    @Override
    public void enable() {
        new MobDatabase();
        MobStorage.resetMobSpawnDelay();
    }

    @Override
    public String getName() {
        return "Storage";
    }
}
