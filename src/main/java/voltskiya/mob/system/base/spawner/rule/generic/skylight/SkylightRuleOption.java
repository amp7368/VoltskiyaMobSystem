package voltskiya.mob.system.base.spawner.rule.generic.skylight;

public enum SkylightRuleOption {
    ONLY_SURFACE,
    ONLY_CAVES;

    public boolean isOnlySurface() {
        return this == ONLY_SURFACE;
    }
}
