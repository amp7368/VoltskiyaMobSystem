package voltskiya.mob.system.base.mob;

import apple.mc.utilities.data.serialize.GsonSerializeMC;
import apple.utilities.database.HasFilename;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDTyped;
import apple.utilities.json.gson.GsonBuilderDynamic;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.spawn.ModuleSpawning;

public class MobTypeDatabase implements HasFilename {

    private static final String MOB_TYPE_PREFIX = "volt.mob.";
    private static final Map<MobUUID, MobType> mobs = new HashMap<>();
    private static final Map<String, MobUUID> nameToMob = new HashMap<>();

    private MobUUID uuid;

    private MobType mob;

    public MobTypeDatabase(MobUUID uuid, MobType mob) {
        this.uuid = uuid;
        this.mob = mob;
    }

    public MobTypeDatabase() {
    }

    public static void load() {
        File file = ModuleSpawning.get().getFile("MobTypes.json");
        AppleAJDTyped<MobTypeDatabase> manager = AppleAJD.createTyped(MobTypeDatabase.class, file, FileIOServiceNow.taskCreator());
        GsonBuilderDynamic gson = GsonSerializeMC.completeGsonDynamicMC();
        manager.setSerializingJson(gson.create());
        for (MobTypeDatabase database : manager.loadFolderNow()) {
            mobs.put(database.uuid, database.mob);
            nameToMob.put(database.mob.getName(), database.uuid);
            database.mob.init();
        }
    }

    public static MobType getMobType(MobUUID uuid) {
        synchronized (mobs) {
            return mobs.get(uuid);
        }
    }

    private static MobUUID getMobUUID(String mobName) {
        synchronized (nameToMob) {
            return nameToMob.get(mobName);
        }
    }

    @Nullable
    public static MobUUID getMobUUID(Entity entity) {
        for (String tag : entity.getScoreboardTags()) {
            if (tag.startsWith(MOB_TYPE_PREFIX)) {
                String mobName = tag.substring(MOB_TYPE_PREFIX.length());
                return getMobUUID(mobName);
            }
        }
        return null;
    }

    @Override
    public String getSaveFileName() {
        return this.uuid.getId() + ".json";
    }
}
