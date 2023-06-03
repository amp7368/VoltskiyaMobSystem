package voltskiya.mob.system.base.selector;

import apple.utilities.database.HasFilename;
import apple.utilities.json.gson.GsonBuilderDynamic;
import apple.utilities.util.FileFormatting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.spawner.LeafSpawner;
import voltskiya.mob.system.base.spawner.Spawner;

public class SpawnSelector implements HasFilename {

    protected Spawner implementation = new Spawner();
    protected List<ExtendsMob> extendsMob = new ArrayList<>();
    /**
     * extendsClause's name is poorly chosen.
     * </p>
     * The mobs in this clause extend this SpawnSelector rather than the other way around
     */
    protected SpawnSelectorGrouping extendsClause = new SpawnSelectorGrouping();
    protected SpawnSelectorUUID uuid;
    protected String name;
    protected transient LeafSpawner cachedSpawner;

    public SpawnSelector(SpawnSelectorUUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public SpawnSelector() {
    }

    public static void initAll(List<SpawnSelectorGrouping> selectorGroups, Collection<SpawnSelector> selectors) {
        selectorGroups = new ArrayList<>(selectorGroups);
        for (SpawnSelector selector : selectors) {
            selectorGroups.add(selector.extendsClause);
            selector.extendsClause.setName(selector.name, selector.uuid);
        }
        for (SpawnSelectorGrouping selector : selectorGroups) {
            try {
                selector.init();
            } catch (CircularDependencyException e) {
                ModuleBase.get().logger().error("Circular dependency!", e);
                // todo do better than print the error
            }
        }
    }

    public static GsonBuilderDynamic gson(GsonBuilderDynamic gson) {
        gson.registerTypeHierarchyAdapter(SpawnSelectorUUID.class, SpawnSelectorUUID.typeAdapter());
        gson.registerTypeHierarchyAdapter(MobUUID.class, MobUUID.typeAdapter());
        return gson;
    }

    public LeafSpawner compiled() {
        if (this.cachedSpawner != null) return this.cachedSpawner;
        return this.cachedSpawner = LeafSpawner.fromBuilt(this.extendsClause.compiled().getLeaves(), this);
    }

    public SpawnSelectorGrouping getExtendsClause() {
        return this.extendsClause;
    }

    public Spawner getImplementation() {
        return this.implementation;
    }


    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SpawnSelector other && this.uuid.equals(other.uuid);
    }

    @Override
    public String getSaveFileName() {
        return FileFormatting.extensionJson(this.uuid.getId().toString());
    }

    public SpawnSelectorUUID getUUID() {
        return this.uuid;
    }

    public Stream<ExtendsMob> getExtendsMobStream() {
        return this.extendsMob.stream();
    }
}
