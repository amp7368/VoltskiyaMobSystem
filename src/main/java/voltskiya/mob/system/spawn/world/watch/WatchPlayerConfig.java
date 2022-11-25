package voltskiya.mob.system.spawn.world.watch;

public class WatchPlayerConfig {

    private static WatchPlayerConfig instance;
    private WatchPlayerMobConfig mob = new WatchPlayerMobConfig();
    public boolean showSummonMob = true;

    public WatchPlayerConfig() {
        instance = this;
    }

    public static WatchPlayerConfig get() {
        return instance;
    }

    public WatchPlayerMobConfig getMob() {
        return this.mob;
    }

    public static class WatchPlayerMobConfig {

        private int spawnChunkRadius = 6;

        public int getSpawningChunkRadius() {
            return spawnChunkRadius;
        }
    }
}
