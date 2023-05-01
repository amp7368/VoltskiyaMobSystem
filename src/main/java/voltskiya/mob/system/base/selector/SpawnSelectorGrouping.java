package voltskiya.mob.system.base.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.spawner.BuiltSpawner;
import voltskiya.mob.system.base.util.UUIDWrapper;

public class SpawnSelectorGrouping {

    protected List<SpawnSelectorUUID> extendsSpawnSelector = new ArrayList<>();
    private transient BuiltSpawner compiled = null;
    private transient String formattedName;

    public void setName(String name, UUIDWrapper<?, ?> uuid) {
        this.formattedName = String.format("%s [%s]", name, uuid.toString());
    }

    public void init() throws CircularDependencyException {
        this.init(new InitializingCallerStack());
    }

    @NotNull
    public BuiltSpawner compiled() {
        return compiled;
    }

    public BuiltSpawner init(InitializingCallerStack callerStack) throws CircularDependencyException {
        if (compiled != null) return compiled;
        callerStack.tryPush(this);
        Set<BuiltSpawner> allSelectors = new HashSet<>();
        for (SpawnSelectorUUID uuid : this.extendsSpawnSelector) {
            SpawnSelector selector = uuid.mapped();
            allSelectors.add(BuiltSpawner.fromOne(selector));
            allSelectors.add(selector.getExtendsClause().init(callerStack));
        }
        callerStack.pop();
        return compiled = BuiltSpawner.fromBuilt(allSelectors);
    }

    public Collection<SpawnSelectorUUID> getSpawnerTags() {
        return Collections.unmodifiableCollection(this.extendsSpawnSelector);
    }

    public String getName() {
        return this.formattedName;
    }
}
