package voltskiya.mob.system.player.world.watch;

public class WatchPlayerConfig {

    private static WatchPlayerConfig instance;
    public boolean showSummonMob = true;
    protected WatchPlayerMobConfig mob = new WatchPlayerMobConfig();

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
