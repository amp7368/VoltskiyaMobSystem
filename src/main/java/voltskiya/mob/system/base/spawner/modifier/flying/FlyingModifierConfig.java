package voltskiya.mob.system.base.spawner.modifier.flying;

import org.bukkit.Location;

public class FlyingModifierConfig {


    private final Location newLocation;

    public FlyingModifierConfig(Location newLocation) {
        this.newLocation = newLocation;
    }

    public Location getNewLocation() {
        return newLocation;
    }
}
