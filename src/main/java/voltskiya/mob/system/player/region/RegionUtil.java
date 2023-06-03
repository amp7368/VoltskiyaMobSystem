package voltskiya.mob.system.player.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import voltskiya.mob.system.player.world.watch.WatchPlayerConfig;

public class RegionUtil {

    public static boolean isInRegion(Location location) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = regionContainer.createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));
        for (ProtectedRegion region : regions) {
            boolean regionExists = WatchPlayerConfig.get().getSpawnableRegions().contains(region.getId());
            if (!regionExists) return true;
        }
        return false;
    }
}
