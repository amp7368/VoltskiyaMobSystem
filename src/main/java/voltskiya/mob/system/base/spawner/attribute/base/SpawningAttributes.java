package voltskiya.mob.system.base.spawner.attribute.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SpawningAttributes {

    private final Set<SpawningAttributeModifier> modifiers;
    private SpawningComputedAttributes computed = null;

    private SpawningAttributes(Collection<SpawningAttributeModifier> modifiers) {
        this.modifiers = new HashSet<>(modifiers);
    }

    public static SpawningAttributes empty() {
        return new SpawningAttributes(Collections.emptyList());
    }

    public static SpawningAttributes of(Collection<SpawningAttributeModifier> modifiers) {
        return new SpawningAttributes(modifiers);
    }

    public SpawningComputedAttributes getComputed() {
        if (computed != null) return computed;
        return computed = new SpawningComputedAttributes(new ArrayList<>(modifiers));
    }

    public void join(SpawningAttributes other) {
        this.modifiers.addAll(other.modifiers);
    }

}
