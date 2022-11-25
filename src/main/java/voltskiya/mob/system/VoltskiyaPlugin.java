package voltskiya.mob.system;


import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.AbstractVoltPlugin;
import java.util.List;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.temperature.ModuleTemperature;

public class VoltskiyaPlugin extends AbstractVoltPlugin {

    public static final int BLOCKS_IN_CHUNK = 16;
    private static VoltskiyaPlugin instance;

    public VoltskiyaPlugin() {
        instance = this;
    }

    public static VoltskiyaPlugin get() {
        return instance;
    }

    @Override
    public List<AbstractModule> getModules() {
        return List.of(new ModuleSpawning(), new ModuleBase(), new ModuleTemperature());
    }
}
