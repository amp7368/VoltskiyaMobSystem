package voltskiya.mob.system.spawn.config;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import voltskiya.mob.system.storage.mob.MobStorage;
import voltskiya.mob.system.storage.world.WorldStorage;

public class RegenStatsMap {

    private static List<RegenStatsMap> maps;
    private final long blockCount;
    private final double desiredDensity;
    private final double hitRatio;
    private final MapRegenConfig config;

    // todo update on insert & delete
    private final int mobCount;

    private final double density;

    public RegenStatsMap(MapRegenConfig config) {
        this.config = config;
        this.mobCount = MobStorage.countMobs(config.getWorld().worldId);
        this.hitRatio = WorldStorage.hitRatio(config.getWorld());
        this.blockCount = (long) config.xRange() * config.yRange() * config.zRange();
        if (blockCount == 0) {
            desiredDensity = density = 0;
            return;
        }
        this.density = mobCount * 1000 / (double) blockCount; // todo update density when mobs are inserted
        this.desiredDensity = 1000 / Math.pow(config.density, 3);
    }

    public static synchronized void load() {
        maps = RegenConfig.get().maps.values().stream().map(RegenStatsMap::new).toList();
    }

    public static synchronized MapRegenConfig chooseMap() {
        if (maps.isEmpty()) throw new IllegalStateException("There are no RegenStatsMap's to choose from");
        return maps.stream()
            .map(m -> Map.entry(m, m.chance()))
            .max(Comparator.comparingDouble(Entry::getValue))
            .orElseThrow()
            .getKey().config;
    }

    private double chance() {
        boolean isBadWorld = this.blockCount == 0 || !this.config.mobSpawningIsOn || this.config.getWorld() == null;
        if (isBadWorld)
            return Double.MIN_VALUE;
        return this.desiredDensity - this.density;
    }
}
