package voltskiya.mob.system.base.selector;

import apple.utilities.database.HasFilename;
import apple.utilities.util.FileFormatting;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.spawner.BuiltSpawner;
import voltskiya.mob.system.base.spawner.Spawner;

public class SpawnSelector implements HasFilename {

    private SpawnSelectorUUID uuid;
    private String name;
    private final Spawner implementation = new Spawner();
    private final SpawnSelectorGrouping extendsClause = new SpawnSelectorGrouping();
    private transient BuiltSpawner cachedSpawner;
    private final transient Set<MobUUID> implementationExtendedByMobs = new HashSet<>();

    public SpawnSelector(SpawnSelectorUUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public SpawnSelector() {
    }

    public static void initAll(Collection<SpawnSelector> selectors) {
        for (SpawnSelector selector : selectors) selector.extendsClause.setName(selector.name, selector.uuid);
        for (SpawnSelector selector : selectors) {
            try {
                selector.extendsClause.init();
            } catch (CircularDependencyException e) {
                ModuleBase.get().logger().error("Circular dependency!", e);
                // todo do better than print the error
            }
        }
    }

    public BuiltSpawner compiled() {
        if (this.cachedSpawner != null) return this.cachedSpawner;
        return this.cachedSpawner = BuiltSpawner.fromBuilt(List.of(this.extendsClause.compiled()), this);
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
        return FileFormatting.extensionJson(name);
    }

    public SpawnSelectorUUID getUUID() {
        return this.uuid;
    }

    public void mobExtends(MobUUID mob) {
        this.implementationExtendedByMobs.add(mob);
    }

    public Collection<MobUUID> getExtendedByMob() {
        return implementationExtendedByMobs.stream().toList();
    }
}
