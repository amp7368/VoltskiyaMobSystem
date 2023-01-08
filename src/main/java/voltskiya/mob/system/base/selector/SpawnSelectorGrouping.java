package voltskiya.mob.system.base.selector;

import java.util.HashSet;
import java.util.Set;
import voltskiya.mob.system.base.spawner.BuiltSpawner;
import voltskiya.mob.system.base.util.UUIDWrapper;

public class SpawnSelectorGrouping {

    private final Set<SpawnSelectorUUID> extendsSpawnSelector = new HashSet<>();
    private transient BuiltSpawner compiled = null;
    private String formattedName;

    public void setName(String name, UUIDWrapper<?, ?> uuid) {
        this.formattedName = String.format("%s [%s]", name, uuid.toString());
    }

    public void addExtends(SpawnSelectorUUID selector) {
        extendsSpawnSelector.add(selector);
    }

    public void init() throws CircularDependencyException {
        this.init(new InitializingCallerStack());
    }

    public BuiltSpawner compiled() {
        Set<BuiltSpawner> allSelectors = new HashSet<>();
        for (SpawnSelectorUUID uuid : this.extendsSpawnSelector) {
            allSelectors.add(uuid.mapped().compiled());
        }
        return BuiltSpawner.fromBuilt(allSelectors);
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

    public String getName() {
        return this.formattedName;
    }
}
