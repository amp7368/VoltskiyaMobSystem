package voltskiya.mob.system.base.selector;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDTyped;
import apple.utilities.json.gson.GsonBuilderDynamic;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.base.spawner.Spawner;

public class SpawnSelectorDatabase {

    public static final GsonBuilderDynamic SELECTOR_GSON = new GsonBuilderDynamic();
    private static final Map<SpawnSelectorUUID, SpawnSelector> selectors = new HashMap<>();
    private static AppleAJDTyped<SpawnSelector> manager;

    static {
        Spawner.registerGson(SELECTOR_GSON);
    }

    public static void load() {
        File file = ModuleBase.get().getFile("SpawnSelectors");

        manager = AppleAJD.createTyped(SpawnSelector.class, file, FileIOServiceNow.taskCreator(), SELECTOR_GSON.create());

        for (SpawnSelector selector : manager.loadFolderNow()) {
            selectors.put(selector.getUUID(), selector);
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
            SpawnSelectorUUID uuid = new SpawnSelectorUUID(UUID.randomUUID());
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
