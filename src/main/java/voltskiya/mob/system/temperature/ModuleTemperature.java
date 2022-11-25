package voltskiya.mob.system.temperature;

import com.voltskiya.lib.AbstractModule;

public class ModuleTemperature extends AbstractModule {

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
