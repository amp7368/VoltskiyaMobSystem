package voltskiya.mob.system.base.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.spawner.BuiltSpawner;
import voltskiya.mob.system.base.spawner.LeafSpawner;
import voltskiya.mob.system.base.util.UUIDWrapper;

public class SpawnSelectorGrouping {

    protected List<SpawnSelectorUUID> extendsSpawnSelector = new ArrayList<>();
    private transient BuiltSpawner compiled = null;
    private transient String formattedName;

    public void setName(String name, UUIDWrapper<?, ?> uuid) {
        this.formattedName = String.format("%s [%s]", name, uuid.toString());
    }

    public void init() throws CircularDependencyException {
        InitializingCallerStack callerStack = new InitializingCallerStack();
        this.init(callerStack);
    }

    @NotNull
    public BuiltSpawner compiled() {
        return compiled;
    }

    public Set<LeafSpawner> init(InitializingCallerStack callerStack) throws CircularDependencyException {
        if (compiled != null) return compiled.getLeaves();
        callerStack.tryPushGroup(this);
        Set<LeafSpawner> leaves = new HashSet<>();
        for (SpawnSelectorUUID uuid : this.extendsSpawnSelector) {
            SpawnSelector selector = uuid.mapped();
            callerStack.pushSelector(selector);
            selector.getExtendsClause().init(callerStack);
            leaves.add(new LeafSpawner(callerStack.copyExtends()));
            callerStack.popSelector();
        }
        callerStack.popGroup();
        compiled = new BuiltSpawner(leaves);
        return leaves;
    }

    public Collection<SpawnSelectorUUID> getSpawnerTags() {
        return Collections.unmodifiableCollection(this.extendsSpawnSelector);
    }

    public String getName() {
        return this.formattedName;
    }

    public void tempRemoveSpawnerTag(SpawnSelectorUUID uuid) {
        this.extendsSpawnSelector.remove(uuid);
    }
}
