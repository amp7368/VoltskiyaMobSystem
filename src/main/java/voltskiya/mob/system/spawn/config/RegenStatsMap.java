package voltskiya.mob.system.spawn.config;

import java.util.Comparator;
import java.util.List;
import voltskiya.mob.system.base.storage.mob.MobStorage;
import voltskiya.mob.system.base.storage.world.WorldStorage;
import voltskiya.mob.system.base.storage.world.WorldUUID;

public class RegenStatsMap {

    private static List<RegenStatsMap> maps;
    private final long blockCount;
    private final double desiredDensity;
    private final double hitRatio;
    private final MapRegenConfig config;

    // todo update on insert & delete
    private int mobCount;

    private double density;

    public static void load() {
        maps = RegenConfig.get().maps.values().stream().map(RegenStatsMap::new).toList();
    }

    public RegenStatsMap(MapRegenConfig config) {
        this.config = config;
        this.mobCount = MobStorage.countMobs(config.getWorld().worldId);
        this.hitRatio = WorldStorage.hitRatio(config.getWorld());
        this.blockCount = (long) config.xRange() * config.yRange() * config.zRange();
        this.density = mobCount * 1000 / (double) blockCount;
        this.desiredDensity = 1000 / Math.pow(config.density, 3);
    }

    public static WorldUUID chooseMap() {
        maps.sort(Comparator.comparingDouble(RegenStatsMap::chance));
        return maps.get(0).config.getWorld();
    }

    private double chance() {
        return this.desiredDensity - this.density;
    }
}
