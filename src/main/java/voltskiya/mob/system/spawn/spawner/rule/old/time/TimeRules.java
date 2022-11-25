package voltskiya.mob.system.spawn.spawner.rule.old.time;

import java.util.HashMap;
import java.util.Map;
import voltskiya.mob.system.spawn.spawner.context.SpawningContext;
import voltskiya.mob.system.spawn.spawner.rule.temporal.SpawningTemporalRule;
import voltskiya.mob.system.spawn.spawner.rule.temporal.TimeOfDay;

public class TimeRules extends SpawningTemporalRule {

    private Map<TimeOfDay, Boolean> allowRules = new HashMap<>() {{
        for (TimeOfDay time : TimeOfDay.values())
            this.put(time, true);
    }};

    public void merge(TimeRules overrideWith) {
        this.allowRules = overrideWith.allowRules;
    }

    @Override
    public long spawnDelay(SpawningContext context) {
        TimeOfDay original = context.timeOfDay();
        Boolean allowNow = this.allowRules.get(original);
        if (allowNow)
            return 0;
        for (TimeOfDay time = original.next(); time != original; time = original.next()) {
            Boolean allow = this.allowRules.get(original);
            if (allow)
                return time.timeUntil(context.time());
        }
        return 24000;
    }
}
