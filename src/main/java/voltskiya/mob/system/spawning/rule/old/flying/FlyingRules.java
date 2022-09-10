package voltskiya.mob.system.spawning.rule.old.flying;

public class FlyingRules {

    private Integer spawnAboveGroundHeight;

    public void merge(FlyingRules overrideWith) {
        if (overrideWith.spawnAboveGroundHeight != null)
            this.spawnAboveGroundHeight = overrideWith.spawnAboveGroundHeight;
    }
}
