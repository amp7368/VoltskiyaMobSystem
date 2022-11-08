package voltskiya.mob.system.base.world;

import apple.lib.pmc.FileIOServiceNow;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import apple.utilities.threading.service.queue.AsyncTaskQueue;
import com.google.common.collect.HashBiMap;
import java.io.File;
import java.util.Map;
import java.util.UUID;
import org.bukkit.World;
import voltskiya.mob.system.spawning.ModuleSpawning;

public class WorldDatabase {

    private static AppleAJDInst<WorldDatabase, AsyncTaskQueue> manager;
    private transient Map<UUID, Short> uuidToId = HashBiMap.create();
    private transient short nextId;
    private Map<Short, UUID> worlds = HashBiMap.create();

    public static void load() {
        File file = ModuleSpawning.get().getFile("Worlds.json");
        manager = AppleAJD.createInst(WorldDatabase.class, file, FileIOServiceNow.taskCreator());
        WorldDatabase db = manager.loadOrMake();
        int nextId = db.worlds.keySet().stream().max(Short::compareTo).orElse((short) -1) + 1;
        db.nextId = (short) nextId;
    }

    public static WorldDatabase get() {
        return manager.getInstance();
    }

    public synchronized short getWorld(UUID uuid) {
        Short id = uuidToId.get(uuid);
        if (id != null)
            return id;
        id = nextId++;
        uuidToId.put(uuid, id);
        worlds.put(id, uuid);
        return id;
    }

    public synchronized UUID getWorld(short id) {
        return worlds.get(id);
    }

    public short getWorld(World world) {
        return this.getWorld(world.getUID());
    }
}
