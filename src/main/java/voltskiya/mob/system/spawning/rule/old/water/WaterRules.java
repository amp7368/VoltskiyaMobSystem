package voltskiya.mob.system.spawning.rule.old.water;

public class WaterRules {

    private Boolean spawnInWater;

    public void merge(WaterRules overrideWith) {
        if (overrideWith.spawnInWater != null)
            this.spawnInWater = overrideWith.spawnInWater;
    }
}
