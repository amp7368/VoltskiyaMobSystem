package voltskiya.mob.system.spawn.world.mob;

public class ShouldSpawningResult {

    public static final ShouldSpawningResult SHOULD_REMOVE = new ShouldSpawningResult(true);
    private boolean shouldRemove = false;
    private long spawnDelay = 0;

    public ShouldSpawningResult(boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
    }

    public ShouldSpawningResult() {
    }

    public long getSpawnDelay() {
        return spawnDelay;
    }

    public void delayUntil(long spawnDelay) {
        this.spawnDelay = Math.max(spawnDelay, this.spawnDelay);
    }

    public boolean shouldSpawn() {
        return shouldRemove && spawnDelay == 0;
    }
}
