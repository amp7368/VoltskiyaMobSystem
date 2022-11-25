package voltskiya.mob.system.base.spawner.rule.old.water;

public class WaterRules {

    private Boolean spawnInWater;

    public void merge(WaterRules overrideWith) {
        if (overrideWith.spawnInWater != null)
            this.spawnInWater = overrideWith.spawnInWater;
    }
}
