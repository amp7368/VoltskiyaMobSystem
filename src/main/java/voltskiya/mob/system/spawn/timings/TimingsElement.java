package voltskiya.mob.system.spawn.timings;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TimingsElement {

    private final String name;
    private boolean didStart = false;
    private boolean didFinish = false;
    private transient long start;
    private long duration;

    public TimingsElement(String name) {
        this.name = name;
    }

    public static String report(TimingsElement[] timings) {
        return "Timings Report [\n" + Arrays.stream(timings)
            .map(TimingsElement::toString)
            .collect(Collectors.joining("\n")) + "]";
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
        return "{ %-20s = %d }".formatted(name, duration / 1000);
    }
}
