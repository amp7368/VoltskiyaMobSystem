package voltskiya.mob.system.spawn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import voltskiya.mob.system.spawn.config.RegenConfig;

public class SpawningStats {

    private final AtomicInteger MOB_COUNT = new AtomicInteger();
    private final AtomicLong lastStart = new AtomicLong();
    private final AtomicLong additionalDuration = new AtomicLong();

    private static long now() {
        return System.currentTimeMillis();
    }

    public void createMob() {
        int count = MOB_COUNT.incrementAndGet();

        int interval = RegenConfig.get().logMobCountInterval;
        ModuleSpawning.get().logger().info("%3.3f mobs/second".formatted(mps()));
        if (interval > 0 && (count + 1) % interval == 0) {
        }
    }

    public double mps() {
        int mobCount = MOB_COUNT.get();
        long duration = now() - lastStart.get() + additionalDuration.get();
        return BigDecimal.valueOf(mobCount)
            .multiply(BigDecimal.valueOf(1000))
            .divide(BigDecimal.valueOf(duration), 5, RoundingMode.UP)
            .doubleValue();
    }

    public synchronized void start() {
        lastStart.set(now());
    }

    public synchronized void stop() {
        additionalDuration.addAndGet(now() - lastStart.get());
    }
}
