package voltskiya.mob.system.base.spawner.rule.generic.skylight;

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
        boolean isSurface = context.location().getBlock().getLightFromSky() > 0;
        return allow.isOnlySurface() == isSurface;
    }
}
