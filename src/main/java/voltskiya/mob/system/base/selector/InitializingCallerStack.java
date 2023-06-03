package voltskiya.mob.system.base.selector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitializingCallerStack {

    private final List<SpawnSelectorGrouping> stack = new ArrayList<>();
    private final Set<SpawnSelectorGrouping> stackSet = new HashSet<>();
    private final List<SpawnSelector> selectorStack = new ArrayList<>();
    private final Set<SpawnSelector> selectorSet = new HashSet<>();

    public String circularDependency() {
        int lastIndex = stack.size() - 1;
        SpawnSelectorGrouping top = stack.get(lastIndex);
        StringBuilder message = new StringBuilder(top.getName()).append(" has a circular dependency!");
        boolean isFirst = true;
        for (int i = 0; i <= lastIndex; i++) {
            SpawnSelectorGrouping element = stack.get(i);
            if (!isFirst) {
                message.append(element.getName());
                message.append("\n");
            }
            isFirst = false;
            message.append(element.getName());
            if (i != lastIndex)
                message.append(" depends on ");
        }
        return message.toString();
    }

    public void tryPushGroup(SpawnSelectorGrouping element) throws CircularDependencyException {
        this.stack.add(element);
        if (!this.stackSet.add(element)) throw new CircularDependencyException(this.circularDependency());
    }

    public void popGroup() {
        int lastIndex = stack.size() - 1;
        stackSet.remove(stack.remove(lastIndex));
    }

    public void pushSelector(SpawnSelector selector) throws CircularDependencyException {
        this.selectorStack.add(selector);
        if (!this.selectorSet.add(selector)) throw new CircularDependencyException(this.circularDependency());
    }

    public void popSelector() {
        int lastIndex = selectorStack.size() - 1;
        selectorSet.remove(selectorStack.remove(lastIndex));
    }

    public Set<SpawnSelector> copyExtends() {
        return new HashSet<>(selectorSet);
    }
}
