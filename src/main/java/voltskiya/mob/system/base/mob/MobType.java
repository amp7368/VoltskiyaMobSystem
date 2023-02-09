package voltskiya.mob.system.base.mob;

import apple.mc.utilities.data.serialize.EntitySerializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import voltskiya.mob.system.base.selector.SpawnSelector;
import voltskiya.mob.system.base.selector.SpawnSelectorUUID;
import voltskiya.mob.system.base.spawner.BuiltSpawner;

public class MobType {

    private MobUUID uuid;
    private String mobName;
    private EntitySerializable entity;
    private Set<SpawnSelectorUUID> spawnerTags = new HashSet<>();
    private transient BuiltSpawner spawner;

    public String getName() {
        return mobName;
    }

    public void spawn(Location location, Consumer<Entity> modifyEntity) {
        entity.spawn(location, modifyEntity);
    }

    public void addSpawnerTag() {
        // todo add a tag to specify all spawners associated with this mob
    }

    public void init() {
        List<BuiltSpawner> parents = new ArrayList<>();
        for (SpawnSelectorUUID tag : spawnerTags) {
            SpawnSelector selector = tag.mapped();
            selector.mobExtends(this.uuid);
            parents.add(selector.compiled());
        }
        spawner = BuiltSpawner.fromBuilt(parents);
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MobType other && this.uuid.equals(other.uuid);
    }

    public MobTypeSpawner getSpawner(BuiltSpawner biomeSpawner) {
        BuiltSpawner spawner = BuiltSpawner.fromBuilt(List.of(biomeSpawner, this.spawner));
        return new MobTypeSpawner(this, spawner);
    }

    public MobUUID getId() {
        return this.uuid;
    }
}

