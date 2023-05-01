package voltskiya.mob.system.base.mob;

import apple.mc.utilities.data.serialize.EntitySerializable;
import apple.utilities.database.HasFilename;
import apple.utilities.json.gson.GsonBuilderDynamic;
import java.util.List;
import org.bukkit.entity.Entity;
import voltskiya.mob.system.base.selector.ExtendsMob;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;
import voltskiya.mob.system.base.selector.SpawnSelectorUUID;
import voltskiya.mob.system.base.spawner.BuiltSpawner;

public class MobType implements HasFilename {

    protected SpawnSelectorGrouping spawnerTags = new SpawnSelectorGrouping();
    protected MobUUID uuid;
    protected String mobName;
    protected EntitySerializable entity;
    protected transient BuiltSpawner spawner;

    public static GsonBuilderDynamic gson(GsonBuilderDynamic gson) {
        gson.registerTypeHierarchyAdapter(SpawnSelectorUUID.class, SpawnSelectorUUID.typeAdapter());
        return gson.registerTypeHierarchyAdapter(MobUUID.class, MobUUID.typeAdapter());
    }

    public String getName() {
        return mobName;
    }

    public EntitySerializable getEntity() {
        return entity;
    }

    public void init() {
        List<BuiltSpawner> parents = spawnerTags.getSpawnerTags()
            .stream()
            .map(tag -> tag.mapped().compiled())
            .toList();
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

    public MobTypeSpawnerInBiome getBiomeSpawner(ExtendsMob extendsMob, BuiltSpawner biomeSpawner) {
        BuiltSpawner spawner = BuiltSpawner.fromBuilt(List.of(biomeSpawner, this.spawner));
        return new MobTypeSpawnerInBiome(this, extendsMob, spawner);
    }

    public MobUUID getId() {
        return this.uuid;
    }

    public BuiltSpawner getRawSpawner() {
        return this.spawner;
    }

    @Override
    public String getSaveFileName() {
        return this.uuid.getId() + ".json";
    }

    public SpawnSelectorGrouping getSpawnerTags() {
        this.spawnerTags.setName(this.getName(), this.uuid);
        return this.spawnerTags;
    }

    public void tagMob(Entity entity) {
        MobTypeDatabase.tagMob(entity, this);
    }

}

