package voltskiya.mob.system.base.spawner.rule.temporal.time;

import java.util.HashMap;
import java.util.Map;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.rule.temporal.SpawningTemporalRule;
import voltskiya.mob.system.base.spawner.rule.temporal.TimeOfDay;

public class TimeRules extends SpawningTemporalRule {

    public Map<TimeOfDay, Boolean> allowedTimes = new HashMap<>() {{
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
        return 0;
//        TimeOfDay now = context.timeOfDay();
//        boolean allowNow = this.allowedTimes.get(now);
//        if (allowNow)
//            return 0;
//        for (TimeOfDay time = now.next(); time != now; time = time.next()) {
//            Boolean allow = this.allowedTimes.get(time);
//            if (allow)
//                return time.timeUntil(context.time());
//        }
//        return 24000;
    }
}
