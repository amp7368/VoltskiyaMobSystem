package voltskiya.mob.system.base.spawner.rule.generic.water;

public enum WaterRuleOption {

    ONLY_WATER,
    ONLY_LAND;

    public boolean isOnlyWater() {
        return this == ONLY_WATER;
    }
}
