package voltskiya.mob.system.spawning.world.watch;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import voltskiya.mob.system.spawning.storage.MobStorage;

public interface MobWatchPlayer extends WatchHasPlayer {

    default void tickWatchPlayer() {
        Player player = getPlayer();
        Location playerLocation = player.getLocation();
        Chunk centerChunk = playerLocation.getChunk();
        int centerChunkX = centerChunk.getX();
        int centerChunkZ = centerChunk.getZ();
        int spawnChunkRadius = WatchPlayerConfig.get().getMob().getSpawningChunkRadius();
        int lowerX = centerChunkX - spawnChunkRadius;
        int upperX = centerChunkX + spawnChunkRadius;
        int lowerZ = centerChunkZ - spawnChunkRadius;
        int upperZ = centerChunkZ + spawnChunkRadius;
        MobStorage.spawnMobs(lowerX, upperX, lowerZ, upperZ, playerLocation.getWorld());
    }
}
