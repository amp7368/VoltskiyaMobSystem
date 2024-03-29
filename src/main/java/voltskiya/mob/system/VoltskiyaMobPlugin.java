package voltskiya.mob.system;


import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.AbstractVoltPlugin;
import java.util.List;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.player.ModulePlayer;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.storage.ModuleStorage;
import voltskiya.mob.system.temperature.ModuleTemperature;

public class VoltskiyaMobPlugin extends AbstractVoltPlugin {

    public static final int BLOCKS_IN_CHUNK = 16;
    private static VoltskiyaMobPlugin instance;

    public VoltskiyaMobPlugin() {
        instance = this;
    }

    public static VoltskiyaMobPlugin get() {
        return instance;
    }

    @Override
    public List<AbstractModule> getModules() {
        return List.of(new ModuleStorage(), new ModuleBase(), new ModuleSpawning(), new ModulePlayer(), new ModuleTemperature());
    }
}
