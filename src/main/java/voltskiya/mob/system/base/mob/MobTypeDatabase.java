package voltskiya.mob.system.base.mob;

import apple.mc.utilities.data.serialize.GsonSerializeMC;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDTyped;
import apple.utilities.json.gson.GsonBuilderDynamic;
import com.google.gson.Gson;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;

public class MobTypeDatabase {

    private static final String MOB_TYPE_ID_PREFIX = "volt.mob.id.";
    private static final String MOB_TYPE_NAME_PREFIX = "volt.mob.name.";
    private static final Map<MobUUID, MobType> mobs = new HashMap<>();
    private static final Map<String, MobUUID> nameToMob = new HashMap<>();

    public static void load() {
        File folder = ModuleBase.get().getFile("MobTypes");
        AppleAJDTyped<MobType> manager = AppleAJD.createTyped(MobType.class, folder, FileIOServiceNow.taskCreator(), gson());

        for (MobType mob : manager.loadFolderNow()) {
            mobs.put(mob.getId(), mob);
            nameToMob.put(mob.getName(), mob.getId());
        }
    }

    @NotNull
    private static Gson gson() {
        GsonBuilderDynamic gson = GsonSerializeMC.completeGsonDynamicMC();
        return MobType.gson(gson).create();
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
            if (tag.startsWith(MOB_TYPE_ID_PREFIX)) {
                String mobId = tag.substring(MOB_TYPE_ID_PREFIX.length());
                try {
                    MobUUID mobUUID = new MobUUID(Integer.parseInt(mobId));
                    if (mobUUID.mapped() != null) return mobUUID;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return null;
    }

    public static void init() {
        mobs.values().forEach(MobType::init);
    }

    public static List<SpawnSelectorGrouping> listSelectors() {
        return mobs.values().stream()
            .map(MobType::getSpawnerTags)
            .toList();
    }


    public static void tagMob(Entity entity, MobType mobType) {
        MobUUID mobId = mobType.getId();
        entity.getScoreboardTags().stream()
            .filter(tag -> tag.startsWith(MOB_TYPE_NAME_PREFIX) || tag.startsWith(MOB_TYPE_ID_PREFIX))
            .forEach(entity::removeScoreboardTag);
        entity.addScoreboardTag(MOB_TYPE_ID_PREFIX + mobId.getId());
        entity.addScoreboardTag(MOB_TYPE_NAME_PREFIX + mobType.getName());
    }
}
