package voltskiya.mob.system.storage.mob.clone;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.storage.mob.clone.query.QDMobClone;

public class MobCloneStorage {

    private static final String CLONE_TAG = "volt.mob.clone";
    private static final String MOB_TAG_PREFIX = "volt.tag.";

    public static boolean hasCloneTag(Entity entity) {
        return entity.getScoreboardTags().contains(CLONE_TAG);
    }

    public static List<DMobTag> getCloneTags(Entity entity) {
        List<DMobTag> tags = new ArrayList<>(2);
        for (String tag : entity.getScoreboardTags()) {
            if (!tag.startsWith(MOB_TAG_PREFIX)) continue;
            String mobTag = tag.substring(MOB_TAG_PREFIX.length());
            tags.add(new DMobTag(mobTag));
        }
        return tags;
    }

    public static void addCloneTags(Entity entity, List<String> tags) {
        entity.setPersistent(false);
        entity.addScoreboardTag(CLONE_TAG);
        for (String tag : tags) {
            entity.addScoreboardTag(getCloneTag(tag));
        }
    }

    @NotNull
    public static String getCloneTag(String tag) {
        return MOB_TAG_PREFIX + tag;
    }

    public static void insertMobs(List<DMobClone> clonesToUnload) {
        clonesToUnload.forEach(mob -> {
            try {
                mob.save();
            } catch (EntityNotFoundException e) {
                mob.insert();
            }
        });
    }

    public static List<DMobClone> queryMobs(List<String> tags) {
        return new QDMobClone().where().tags.tag.in(tags).findList();
    }
}
