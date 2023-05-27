package voltskiya.mob.system.base.spawner.rule.generic.elevation;

import apple.utilities.util.NumberUtils;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.rule.generic.GsonMapSpawningRule;
import voltskiya.mob.system.base.spawner.rule.generic.SpawningRule;

public class ElevationRules extends SpawningRule {

    protected int minElevation = Integer.MIN_VALUE;
    protected int maxElevation = Integer.MAX_VALUE;

    public ElevationRules() {
        super(GsonMapSpawningRule.ELEVATION.getTypeId());
    }

    public void merge(ElevationRules overrideWith) {
        this.minElevation = overrideWith.minElevation;
        this.maxElevation = overrideWith.minElevation;
    }

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        int y = context.location().getBlockY();
        return !NumberUtils.betweenInclusive(minElevation, y, maxElevation);
    }
}
