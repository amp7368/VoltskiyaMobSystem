package voltskiya.mob.system.base.spawner.rule.temporal.time;

import java.util.HashMap;
import java.util.Map;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.rule.temporal.SpawningTemporalRule;
import voltskiya.mob.system.base.spawner.rule.temporal.TimeOfDay;

public class TimeRules extends SpawningTemporalRule {

    private Map<TimeOfDay, Boolean> allowedTimes = new HashMap<>() {{
        for (TimeOfDay time : TimeOfDay.values())
            this.put(time, true);
    }};

    public TimeRules() {
        super("time");
    }

    public void merge(TimeRules overrideWith) {
        this.allowedTimes = overrideWith.allowedTimes;
    }

    @Override
    public long spawnDelay(SpawningContext context) {
        TimeOfDay original = context.timeOfDay();
        boolean allowNow = this.allowedTimes.get(original);
        if (allowNow)
            return 0;
        for (TimeOfDay time = original.next(); time != original; time = time.next()) {
            Boolean allow = this.allowedTimes.get(original);
            if (allow)
                return time.timeUntil(context.time());
        }
        return 24000;
    }
}
