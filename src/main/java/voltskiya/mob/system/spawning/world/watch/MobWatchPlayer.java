package voltskiya.mob.system.spawning.world.watch;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.spawning.storage.MobStorage;
import voltskiya.mob.system.spawning.storage.SpawnMobsRegion;

public interface MobWatchPlayer extends WatchHasPlayer {

    default void tickWatchPlayer() {
        Player player = getPlayer();
        Location playerLocation = player.getLocation();
        Chunk centerChunk = playerLocation.getChunk();
        int centerChunkX = centerChunk.getX();
        int centerChunkZ = centerChunk.getZ();
        int spawnChunkRadius = WatchPlayerConfig.get().getMob().getSpawningChunkRadius();
        SpawnMobsRegion region = new SpawnMobsRegion(centerChunkX, centerChunkZ, spawnChunkRadius,
            playerLocation.getWorld());
        queue(() -> MobStorage.spawnMobs(region));
    }
}
