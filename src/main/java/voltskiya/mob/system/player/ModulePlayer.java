package voltskiya.mob.system.player;

import com.voltskiya.lib.AbstractModule;
import voltskiya.mob.system.player.world.watch.WatchPlayer;
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
}
