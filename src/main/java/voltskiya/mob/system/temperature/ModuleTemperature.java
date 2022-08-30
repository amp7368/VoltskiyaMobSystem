package voltskiya.mob.system.temperature;

import apple.lib.pmc.PluginModule;

public class ModuleTemperature extends PluginModule {

    private static ModuleTemperature instance;

    public ModuleTemperature() {
        instance = this;
    }

    public static ModuleTemperature get() {
        return instance;
    }

    @Override
    public void enable() {

    }

    @Override
    public String getName() {
        return "Temperature";
    }
}
