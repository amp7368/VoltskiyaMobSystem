package voltskiya.mob.system.spawn.spawner.rule.temporal;

public enum TimeOfDay {
    MORNING(0),
    NOON(1),
    EVENING(2),
    MIDNIGHT(3);
    private static TimeOfDay[] times;

    private final int index;

    TimeOfDay(int index) {
        this.index = index;
    }

    public static TimeOfDay getTime(long time) {
        time += 6000;
        time %= 24000;
        if (time < 3000 || time >= 21000) {
            return MIDNIGHT;
        } else if (time < 9000) {
            return MORNING;
        } else if (time < 15000) {
            return NOON;
        } else {
            return EVENING;
        }
    }

    public TimeOfDay next() {
        int timeLength = TimeOfDay.values().length;
        int nextIndex = (index + 1) % timeLength;
        return getOrderedTimes()[nextIndex];
    }

    private static TimeOfDay[] getOrderedTimes() {
        if (times != null)
            return times;
        times = new TimeOfDay[TimeOfDay.values().length];
        for (TimeOfDay time : TimeOfDay.values())
            times[time.index] = time;
        return times;
    }

    public long timeUntil(long time) {
        return 0;
    }
}