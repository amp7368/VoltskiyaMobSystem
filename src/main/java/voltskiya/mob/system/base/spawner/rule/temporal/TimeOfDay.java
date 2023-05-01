package voltskiya.mob.system.base.spawner.rule.temporal;

public enum TimeOfDay {
    MORNING(),
    NOON(),
    EVENING(),
    MIDNIGHT();
    private static TimeOfDay[] times;

    private int index;

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

    private synchronized static TimeOfDay[] getOrderedTimes() {
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
        return 0; //todo
    }
}