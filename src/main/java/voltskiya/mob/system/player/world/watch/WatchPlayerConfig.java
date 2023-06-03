package voltskiya.mob.system.player.world.watch;

import java.util.HashSet;
import java.util.Set;

public class WatchPlayerConfig {

    private static WatchPlayerConfig instance;
    protected WatchPlayerMobConfig mob = new WatchPlayerMobConfig();
    private Set<String> spawnableRegions = new HashSet<>();

    public WatchPlayerConfig() {
        instance = this;
    }

    public static WatchPlayerConfig get() {
        return instance;
    }

    public WatchPlayerMobConfig getMob() {
        return this.mob;
    }

    public Set<String> getSpawnableRegions() {
        return spawnableRegions;
    }

    public static class WatchPlayerMobConfig {

        private int spawnChunkRadius = 6;

        public int getSpawningChunkRadius() {
            return spawnChunkRadius;
        }
    }
}
