package voltskiya.mob.system.spawn.task;

public class TimingsElement {

    private final String name;
    private boolean didStart = false;
    private boolean didFinish = false;
    private transient long start;
    private long duration;

    public TimingsElement(String name) {
        this.name = name;
    }

    public void mark() {
        if (didFinish) {
            throw new IllegalStateException("Already finished " + name + " timings");
        }
        if (didStart) {
            duration = System.nanoTime() - start;
            didFinish = true;
        } else {
            didStart = true;
            start = System.nanoTime();
        }
    }

    @Override
    public String toString() {
        return "{%-20s=%d}".formatted(name, duration);
    }
}
