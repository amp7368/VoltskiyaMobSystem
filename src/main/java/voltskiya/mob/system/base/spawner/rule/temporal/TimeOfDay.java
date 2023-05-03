package voltskiya.mob.system.base.spawner.rule.temporal;

public enum TimeOfDay {
    MORNING(3000),
    NOON(9000),
    EVENING(15000),
    MIDNIGHT(21000);
    private static TimeOfDay[] times;

    static {
        getOrderedTimes();
    }

    private final int earliest;

    private int index;

    TimeOfDay(int earliest) {
        this.earliest = earliest;
    }

    public static TimeOfDay getTime(long time) {
        time += 24000 + 6000;
        time %= 24000;
        if (time < MORNING.earliest || time >= MIDNIGHT.earliest) {
            return MIDNIGHT;
        } else if (time < NOON.earliest) {
            return MORNING;
        } else if (time < EVENING.earliest) {
            return NOON;
        } else {
            return EVENING;
        }
    }

    private static TimeOfDay[] getOrderedTimes() {
        if (times != null)
            return times;
        times = values();
        for (int i = 0; i < times.length; i++) {
            times[i].index = i;
        }
        return times;
    }

    public TimeOfDay next() {
        int timeLength = getOrderedTimes().length;
        int nextIndex = (index + 1) % timeLength;
        return getOrderedTimes()[nextIndex];
    }

    public long timeUntil(long time) {
        // add a day to verify positive
        return (this.earliest - time + 24000) % 24000;
    }
}