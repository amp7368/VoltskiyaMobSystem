package voltskiya.mob.system.spawn.config;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import voltskiya.mob.system.spawn.ModuleSpawning;

public class RegenConfig {

    private static RegenConfig instance;
    private static AppleAJDInst<RegenConfig> manager;
    public int maxPlayersWhileRunning = 0;

    public Map<UUID, MapRegenConfig> maps = new HashMap<>();

    public RegenConfig() {
        instance = this;
        for (World world : Bukkit.getWorlds()) {
            maps.put(world.getUID(), new MapRegenConfig(world));
        }
    }

    public static void load() {
        File file = ModuleSpawning.get().getFile("WorldRegenConfig.json");
        manager = AppleAJD.createInst(RegenConfig.class, file, FileIOServiceNow.taskCreator());
    }

    public static RegenConfig get() {
        return instance;
    }

    public static void save() {
        manager.save();
    }
}
