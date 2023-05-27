package voltskiya.mob.system.storage.world;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.World;
import voltskiya.mob.system.spawn.ModuleSpawning;

public class WorldAdapter {

    private static AppleAJDInst<WorldAdapter> manager;
    private final transient BiMap<UUID, WorldUUID> uuidToId = HashBiMap.create();
    protected Map<Short, WorldUUID> worlds = new HashMap<>();
    private transient short nextId;

    public static void load() {
        File file = ModuleSpawning.get().getFile("Worlds.json");
        manager = AppleAJD.createInst(WorldAdapter.class, file, FileIOServiceNow.taskCreator());
        WorldAdapter db = manager.loadOrMake();
        int nextId = db.worlds.keySet().stream().max(Short::compareTo).orElse((short) -1) + 1;
        db.nextId = (short) nextId;
        for (WorldUUID world : db.worlds.values())
            db.uuidToId.put(world.uuid, world);
    }

    public static WorldAdapter get() {
        return manager.getInstance();
    }

    private static void save() {
        manager.saveNow();
    }

    public synchronized WorldUUID getWorld(UUID uuid) {
        WorldUUID id = uuidToId.get(uuid);
        if (id != null)
            return id;
        id = new WorldUUID(nextId++, uuid);
        uuidToId.put(uuid, id);
        worlds.put(id.worldId, id);
        save();
        return id;
    }


    public synchronized WorldUUID getWorld(short id) {
        return worlds.get(id);
    }

    public WorldUUID getWorld(World world) {
        return this.getWorld(world.getUID());
    }
}
