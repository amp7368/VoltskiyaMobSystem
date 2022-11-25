package voltskiya.mob.system.base.mob;

import apple.mc.utilities.data.serialize.GsonSerializeMC;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import apple.utilities.json.gson.GsonBuilderDynamic;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.spawn.ModuleSpawning;

public class MobTypeDatabase {

    private final String MOB_TYPE_PREFIX = "volt.mob.";
    private static AppleAJDInst<MobTypeDatabase> manager;

    private final Map<MobUUID, MobType> mobs = new HashMap<>();
    private transient final Map<String, MobUUID> nameToMob = new HashMap<>();

    public static void load() {
        File file = ModuleSpawning.get().getFile("MobTypes.json");
        manager = AppleAJD.createInst(MobTypeDatabase.class, file, FileIOServiceNow.taskCreator());
        GsonBuilderDynamic gson = GsonSerializeMC.completeGsonDynamicMC();
        MobType.gson(gson);
        manager.setSerializingJson(gson.create());
        MobTypeDatabase inst = manager.loadOrMake();
        for (Entry<MobUUID, MobType> mob : inst.mobs.entrySet()) {
            inst.nameToMob.put(mob.getValue().getName(), mob.getKey());
        }
    }

    public static MobTypeDatabase get() {
        return manager.getInstance();
    }

    public MobType getMobType(MobUUID uuid) {
        synchronized (mobs) {
            return mobs.get(uuid);
        }
    }

    private MobUUID getMobUUID(String mobName) {
        synchronized (nameToMob) {
            return nameToMob.get(mobName);
        }
    }

    @Nullable
    public MobUUID getMobUUID(Entity entity) {
        for (String tag : entity.getScoreboardTags()) {
            if (tag.startsWith(MOB_TYPE_PREFIX)) {
                String mobName = tag.substring(MOB_TYPE_PREFIX.length());
                return getMobUUID(mobName);
            }
        }
        return null;
    }
}
