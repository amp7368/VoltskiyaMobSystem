package voltskiya.mob.system.base.selector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitializingCallerStack {

    private final List<SpawnSelectorGrouping> stack = new ArrayList<>();
    private final Set<SpawnSelectorGrouping> stackSet = new HashSet<>();

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

    public void tryPush(SpawnSelectorGrouping element) throws CircularDependencyException {
        this.stack.add(element);
        if (!this.stackSet.add(element)) throw new CircularDependencyException(this.circularDependency());
    }

    public void pop() {
        int lastIndex = stack.size() - 1;
        stackSet.remove(stack.remove(lastIndex));
    }
}
