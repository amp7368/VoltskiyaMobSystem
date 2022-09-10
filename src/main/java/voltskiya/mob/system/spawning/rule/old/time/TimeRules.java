package voltskiya.mob.system.spawning.rule.old.time;

public class TimeRules {

    private Boolean allowDay;
    private Boolean allowEvening;
    private Boolean allowNight;
    private Boolean allowMorning;

    public void merge(TimeRules overrideWith) {
        if (overrideWith.allowDay != null)
            this.allowDay = overrideWith.allowDay;
        if (overrideWith.allowEvening != null)
            this.allowEvening = overrideWith.allowEvening;
        if (overrideWith.allowNight != null)
            this.allowNight = overrideWith.allowNight;
        if (overrideWith.allowMorning != null)
            this.allowMorning = overrideWith.allowMorning;
    }
}
