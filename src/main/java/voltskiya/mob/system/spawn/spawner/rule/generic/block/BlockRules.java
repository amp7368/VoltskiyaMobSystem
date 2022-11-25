package voltskiya.mob.system.spawn.spawner.rule.generic.block;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import voltskiya.mob.system.spawn.spawner.context.SpawningContext;
import voltskiya.mob.system.spawn.spawner.rule.generic.SpawningRule;

public abstract class BlockRules extends SpawningRule {

    private final Set<Material> blocks = new HashSet<>();

    public boolean isBlockContained(SpawningContext context) {
        Material belowFeet = context.belowFeetBlock().getType();
        return blocks.contains(belowFeet);
    }
}
