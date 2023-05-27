package voltskiya.mob.system.base.spawner.rule.generic.skylight;

import apple.mc.utilities.item.material.MaterialUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.rule.generic.GsonMapSpawningRule;
import voltskiya.mob.system.base.spawner.rule.generic.SpawningRule;

public class SkylightRule extends SpawningRule {

    protected SkylightRuleOption allow;

    public SkylightRule() {
        super(GsonMapSpawningRule.SKYLIGHT.getTypeId());
    }

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        Location location = context.location().clone();
        boolean isSurface = location.getBlock().getLightFromSky() > 0;
        if (isSurface) return allow.isOnlySurface() != isSurface;
        int maxHeight = location.getWorld().getMaxHeight();
        while (true) {
            Material material = location.getBlock().getType();
            if (material.isAir()) {
                isSurface = location.getBlock().getLightFromSky() > 0;
                return allow.isOnlySurface() != isSurface;
            }
            if (material != Material.WATER && !MaterialUtils.isWalkThroughable(material)) {
                // not surface
                return allow.isOnlySurface();
            }
            location.add(0, 1, 0);
            if (maxHeight <= location.getBlockY()) {
                // not surface
                return allow.isOnlySurface();
            }
        }

    }
}
