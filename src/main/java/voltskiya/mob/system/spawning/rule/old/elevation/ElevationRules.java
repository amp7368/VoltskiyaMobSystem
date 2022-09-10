package voltskiya.mob.system.spawning.rule.old.elevation;

public class ElevationRules {

    private Integer minElevation;
    private Integer maxElevation;

    public void merge(ElevationRules overrideWith) {
        if (overrideWith.minElevation != null)
            this.minElevation = overrideWith.minElevation;
        if (overrideWith.maxElevation != null)
            this.maxElevation = overrideWith.minElevation;
    }
}
