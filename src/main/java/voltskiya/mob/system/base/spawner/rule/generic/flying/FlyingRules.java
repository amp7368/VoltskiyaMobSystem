package voltskiya.mob.system.base.spawner.rule.generic.flying;

import apple.mc.utilities.item.material.MaterialUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.rule.generic.GsonMapSpawningRule;
import voltskiya.mob.system.base.spawner.rule.generic.SpawningRule;

public class FlyingRules extends SpawningRule {

    protected int spawnAboveGroundHeight = 0;

    public FlyingRules() {
        super(GsonMapSpawningRule.FLYING.getTypeId());
    }

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        int groundY = context.location().getBlockY();
        int maxY = spawnAboveGroundHeight + groundY;
        Location current = context.location();
        for (int y = groundY; y < maxY; y++) {
            current.add(0, 1, 0);
            Material material = current.getBlock().getType();
            if (isFailAboveGround(material))
                return true;
        }
        return false;
    }

    private boolean isFailAboveGround(Material material) {
        if (material.isAir())
            return false;
        if (MaterialUtils.isTree(material))
            return false;
        return !MaterialUtils.isWalkThroughable(material);
    }
}
