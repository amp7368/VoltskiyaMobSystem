package voltskiya.mob.system.base.spawner.rule.generic.elevation;

import apple.utilities.util.NumberUtils;
import voltskiya.mob.system.base.spawner.rule.generic.SpawningRule;
import voltskiya.mob.system.base.spawner.context.SpawningContext;

public class ElevationRules extends SpawningRule {

    private int minElevation;
    private int maxElevation;

    public void merge(ElevationRules overrideWith) {
        this.minElevation = overrideWith.minElevation;
        this.maxElevation = overrideWith.minElevation;
    }

    @Override
    public boolean isBreaksRule(SpawningContext context) {
        int y = context.location().getBlockY();
        return NumberUtils.betweenInclusive(minElevation, y, maxElevation);
    }
}
