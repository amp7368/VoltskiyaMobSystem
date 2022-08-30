package voltskiya.mob.system.spawning.conditions;

public enum TimeOfDay {
    MORNING,
    NOON,
    EVENING,
    MIDNIGHT;

    public static TimeOfDay getTime(long time) {
        time += 6000;
        time %= 24000;
        if (time < 3000) {
            return MIDNIGHT;
        } else if (time < 9000) {
            return MORNING;
        } else if (time < 16000) {
            return NOON;
        } else {
            return EVENING;
        }
    }
}