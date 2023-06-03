package voltskiya.mob.system.spawn.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.storage.mob.MobStorage;
import voltskiya.mob.system.storage.world.WorldStorage;

public class RegenStatsMap {

    private static final int UPDATE_INTERVAL = 1000;
    private static final AtomicInteger INCREMENT = new AtomicInteger();
    private static List<RegenStatsMap> maps;
    public final MapRegenConfig config;
    private final BigDecimal blockCount;
    private final BigDecimal desiredDensity;
    private final double hitRatio;
    private BigDecimal density;

    public RegenStatsMap(MapRegenConfig config) {
        this.config = config;
        this.hitRatio = WorldStorage.hitRatio(config.getWorld());
        this.blockCount = BigDecimal.valueOf((long) config.xRange() * config.yRange() * config.zRange());
        this.desiredDensity = BigDecimal.valueOf(config.density)
            .divide(BigDecimal.valueOf(10_000_000), Double.SIZE, RoundingMode.HALF_UP);
        updateMobCount();

    }

    public static synchronized void load() {
        maps = RegenConfig.get().maps.values().stream().map(RegenStatsMap::new).toList();
    }

    public static synchronized RegenStatsMap chooseMap() {
        if (maps.isEmpty()) throw new IllegalStateException("There are no RegenStatsMap's to choose from");
        if (INCREMENT.incrementAndGet() % UPDATE_INTERVAL == 0) {
            maps.forEach(RegenStatsMap::updateMobCount);
        }
        RegenStatsMap map = maps.stream()
            .max(Comparator.comparing(RegenStatsMap::chance))
            .orElseThrow();
        if (map.chance().compareTo(BigDecimal.ZERO) <= 0) return null;
        return map;
    }

    private void updateMobCount() {
        int mobCount = MobStorage.countMobs(config.getWorld().worldId);
        if (blockCount.equals(BigDecimal.ZERO)) {
            density = BigDecimal.ZERO;
            return;
        }
        this.density = BigDecimal.valueOf(mobCount)
            .divide(blockCount, Double.SIZE, RoundingMode.HALF_UP);
        if (config.mobSpawningIsOn && this.desiredDensity.compareTo(this.density) > 0) {
            ModuleSpawning.get().logger().info("desiredDensity " + this.desiredDensity);
            ModuleSpawning.get().logger().info("density " + this.density);
        }
    }

    public boolean isDensityTooHigh(double density) {
        return desiredDensity.doubleValue() < density;
    }

    private BigDecimal chance() {
        boolean isBadWorld = this.blockCount.equals(BigDecimal.ZERO) ||
            !this.config.mobSpawningIsOn ||
            this.config.getWorld() == null;
        if (isBadWorld)
            return BigDecimal.ZERO;
        return this.desiredDensity.subtract(this.density);
    }
}
