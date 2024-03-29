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

    private static AppleAJDInst<RegenConfig> manager;
    public int maxPlayersWhileRunning = 0;
    public int maxThreadPool = 5;

    public Map<UUID, MapRegenConfig> maps = new HashMap<>();
    public boolean logMobSpawn = true;
    public boolean logMobSummon = true;
    public int logMobCountInterval = 20;
    protected boolean doTimings = false;

    public static void load() {
        File file = ModuleSpawning.get().getFile("WorldRegenConfig.json");
        manager = AppleAJD.createInst(RegenConfig.class, file, FileIOServiceNow.taskCreator());
        manager.loadOrMake().init();
        manager.saveNow();
    }

    public static RegenConfig get() {
        return manager.getInstance();
    }

    public static void save() {
        manager.save();
    }

    public void init() {
        for (World world : Bukkit.getWorlds()) {
            maps.putIfAbsent(world.getUID(), new MapRegenConfig(world));
        }
        maps.values().forEach(MapRegenConfig::init);
    }

    public boolean shouldDoTimings() {
        return doTimings;
    }
}
