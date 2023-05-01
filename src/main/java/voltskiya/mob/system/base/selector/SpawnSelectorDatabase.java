package voltskiya.mob.system.base.selector;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDTyped;
import apple.utilities.json.gson.GsonBuilderDynamic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.base.spawner.Spawner;

public class SpawnSelectorDatabase extends HashMap<SpawnSelectorUUID, SpawnSelector> {

    private static final Map<SpawnSelectorUUID, SpawnSelector> selectors = new HashMap<>();
    private static AppleAJDTyped<SpawnSelector> manager;

    public static void load() {
        File folder = ModuleBase.get().getFile("SpawnSelectors");
        manager = AppleAJD.createTyped(SpawnSelector.class, folder, FileIOServiceNow.taskCreator(), gson());

        for (SpawnSelector selector : manager.loadFolderNow()) {
            selectors.put(selector.getUUID(), selector);
        }
    }

    public static Gson gson() {
        GsonBuilderDynamic gson = new GsonBuilderDynamic().withBaseBuilder(() -> new GsonBuilder().enableComplexMapKeySerialization());
        SpawnSelector.gson(gson);
        return Spawner.gson(gson).create();
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

    public static Collection<SpawnSelector> listSelectors() {
        synchronized (selectors) {
            return Collections.unmodifiableCollection(selectors.values());
        }
    }
}
