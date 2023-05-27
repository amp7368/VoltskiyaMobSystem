package voltskiya.mob.system.player.world.mob;

import org.bukkit.Bukkit;

public class ShouldSpawningResult {

    public static final ShouldSpawningResult SHOULD_REMOVE = new ShouldSpawningResult(true);
    private boolean shouldRemove = false;
    private long spawnDelay = 0;

    private ShouldSpawningResult(boolean shouldRemove) {
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
        return canFutureSpawn() && spawnDelay <= Bukkit.getCurrentTick();
    }

    public boolean canFutureSpawn() {
        return !shouldRemove;
    }

    @Override
    public String toString() {
        return "ShouldSpawningResult{" +
            "shouldRemove=" + shouldRemove +
            ", spawnDelay=" + spawnDelay +
            '}';
    }
}
