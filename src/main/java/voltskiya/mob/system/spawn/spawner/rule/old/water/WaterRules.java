package voltskiya.mob.system.spawn.spawner.rule.old.water;

public class WaterRules {

    private Boolean spawnInWater;

    public void merge(WaterRules overrideWith) {
        if (overrideWith.spawnInWater != null)
            this.spawnInWater = overrideWith.spawnInWater;
    }
}
