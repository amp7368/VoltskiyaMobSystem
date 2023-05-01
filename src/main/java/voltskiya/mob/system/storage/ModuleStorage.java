package voltskiya.mob.system.storage;

import com.voltskiya.lib.AbstractModule;

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
        VoltskiyaDatabase.load();
    }

    @Override
    public String getName() {
        return "Storage";
    }
}
