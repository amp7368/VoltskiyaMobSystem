package voltskiya.mob.system.player.region;

import apple.mc.utilities.player.chat.SendMessage;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.voltskiya.lib.acf.BaseCommand;
import com.voltskiya.lib.acf.annotation.CommandAlias;
import com.voltskiya.lib.acf.annotation.CommandCompletion;
import com.voltskiya.lib.acf.annotation.CommandPermission;
import com.voltskiya.lib.acf.annotation.Default;
import com.voltskiya.lib.acf.annotation.Name;
import java.util.Collections;
import java.util.Map;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.VoltskiyaMobPlugin;

@CommandAlias("region_copy")
@CommandPermission("worldguard.region")
public class RegionCopyCommand extends BaseCommand implements SendMessage {

    public RegionCopyCommand() {
        VoltskiyaMobPlugin.get().registerCommand(this);
        VoltskiyaMobPlugin.get().getCommandManager().getCommandCompletions().registerAsyncCompletion("world_guard_region", (c) -> {
            RegionManager regionManager = getRegionManager(c.getPlayer());
            if (regionManager == null) return Collections.emptyList();
            return regionManager.getRegions().keySet();
        });
    }

    @Nullable
    private static RegionManager getRegionManager(Player player) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        return regionContainer.get(BukkitAdapter.adapt(player.getWorld()));
    }

    @Default
    @CommandCompletion("@world_guard_region|[from_region] @world_guard_region|[to_region] @nothing")
    public void regionCopy(Player player, @Name("from") String regionFromArg, @Name("to") String regionToArg) {
        RegionManager regionManager = getRegionManager(player);
        if (regionManager != null) {
            ProtectedRegion regionFrom = regionManager.getRegion(regionFromArg);
            ProtectedRegion regionTo = regionManager.getRegion(regionToArg);
            if (regionFrom == null) {
                red(player, "No region matching " + regionFromArg);
                return;
            }
            if (regionTo == null) {
                red(player, "No region matching " + regionToArg);
                return;
            }
            Map<Flag<?>, Object> flags = regionFrom.getFlags();
            regionTo.setFlags(flags);
            aqua(player, "Successfuly copied flags");
        }

    }

}
