package voltskiya.mob.system.base.mob;

import apple.mc.utilities.data.serialize.EntitySerializable;
import apple.utilities.json.gson.GsonBuilderDynamic;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import voltskiya.mob.system.base.spawner.MobTypeSpawner;

public class MobType {

    private MobUUID mobUUID;
    private String mobName;
    private MobTypeSpawner globalSpawning;
    private EntitySerializable entity;

    public static void gson(GsonBuilderDynamic gson) {
        MobTypeSpawner.gson(gson);
    }

    public MobTypeSpawner getSpawner() {
        return globalSpawning;
    }

    public String getName() {
        return mobName;
    }

    public void spawn(Location location, Consumer<Entity> modifyEntity) {
        entity.spawn(location, modifyEntity);
    }

}

