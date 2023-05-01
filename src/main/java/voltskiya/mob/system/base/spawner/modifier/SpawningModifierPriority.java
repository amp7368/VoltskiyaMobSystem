package voltskiya.mob.system.base.spawner.modifier;

public enum SpawningModifierPriority {
    FIRST(-20),
    EARLIER(-10),
    EARLY(-5),
    NORMAL(0),
    LATE(5),
    LATER(10),
    LAST(20);

    private final int value;

    SpawningModifierPriority(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
