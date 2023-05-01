package voltskiya.mob.system.player;

import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.configs.factory.AppleConfigLike;
import java.util.List;
import voltskiya.mob.system.player.world.watch.WatchPlayer;
import voltskiya.mob.system.player.world.watch.WatchPlayerConfig;
import voltskiya.mob.system.player.world.watch.WatchPlayerListener;

public class ModulePlayer extends AbstractModule {

    @Override
    public void enable() {
        new WatchPlayerListener();
        WatchPlayer.load();
    }

    @Override
    public String getName() {
        return "Player";
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(WatchPlayerConfig.class, "WatchPlayerConfig"));

    }
}
