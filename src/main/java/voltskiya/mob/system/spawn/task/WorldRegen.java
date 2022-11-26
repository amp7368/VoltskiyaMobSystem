package voltskiya.mob.system.spawn.task;

import voltskiya.mob.system.base.storage.world.WorldUUID;
import voltskiya.mob.system.spawn.config.RegenStatsMap;

public class WorldRegen {

    public void run() {
        WorldUUID world = RegenStatsMap.chooseMap();
    }
}
