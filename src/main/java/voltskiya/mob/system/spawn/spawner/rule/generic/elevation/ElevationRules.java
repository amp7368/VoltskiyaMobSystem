package voltskiya.mob.system.spawn.spawner.rule.generic.elevation;

import apple.utilities.util.NumberUtils;
import voltskiya.mob.system.spawn.spawner.context.SpawningContext;
import voltskiya.mob.system.spawn.spawner.rule.generic.SpawningRule;

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
