package voltskiya.mob.system.spawn.task;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import voltskiya.mob.system.base.ModuleBase;

public class BiomeErrors {

    private static AppleAJDInst<BiomeErrors> manager;
    private static BiomeErrors instance;
    protected Set<String> missing = new HashSet<>();
    protected Set<String> biomeTypeDuplicates = new HashSet<>();

    public BiomeErrors() {
        instance = this;
    }

    public static void load() {
        File file = ModuleBase.get().getFile("BiomeErrors.json");
        manager = AppleAJD.createInst(BiomeErrors.class, file, FileIOServiceNow.taskCreator());
        manager.loadOrMake();
        instance.biomeTypeDuplicates = new HashSet<>();
        manager.saveNow();
    }

    public static BiomeErrors get() {
        return instance;
    }

    public synchronized void add(String key) {
        if (missing.add(key))
            manager.saveNow();
    }

    public void addDuplicateType(String key) {
        if (biomeTypeDuplicates.add(key))
            manager.saveNow();
    }
}
