package voltskiya.mob.system.base.selector;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDTyped;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import voltskiya.mob.system.base.ModuleBase;

public class SpawnSelectorDatabase {

    private static AppleAJDTyped<SpawnSelector> manager;
    private static final Map<SpawnSelectorUUID, SpawnSelector> selectors = new HashMap<>();
    private static int nextId = 0;

    public static void load() {
        File file = ModuleBase.get().getFile("SpawnSelectors");
        manager = AppleAJD.createTyped(SpawnSelector.class, file, FileIOServiceNow.taskCreator());

        for (SpawnSelector selector : manager.loadFolderNow()) {
            selectors.put(selector.getUUID(), selector);
            int id = selector.getUUID().getId();
            if (id >= nextId) {
                nextId = id + 1;
            }
        }
        SpawnSelector.initAll(selectors.values());
    }


    public static SpawnSelector get(SpawnSelectorUUID uuid) {
        synchronized (selectors) {
            return selectors.get(uuid);
        }
    }

    public static void add(String name) {
        synchronized (selectors) {
            SpawnSelectorUUID uuid = new SpawnSelectorUUID(nextId++);
            SpawnSelector selector = new SpawnSelector(uuid, name);
            selectors.put(uuid, selector);
            save(selector);
        }
    }

    public static void save(SpawnSelector selector) {
        synchronized (selectors) {
            manager.saveInFolderNow(selector);
        }
    }
}
