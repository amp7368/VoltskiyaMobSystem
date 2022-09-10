package voltskiya.mob.system.spawning.world.mob;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import voltskiya.mob.system.spawning.context.SpawningContext;
import voltskiya.mob.system.spawning.modifier.entity.SpawningModifier;

public class SpawnerSummonResult {

    private final Location location;
    private final List<SpawningModifier> entityModifiers = new ArrayList<>();

    public SpawnerSummonResult(SpawningContext context) {
        this.location = context.location();
    }

    public Location getLocation() {
        return this.location;
    }

    public void addModifier(SpawningModifier modifier) {
        entityModifiers.add(modifier);
    }

    public void modifyEntity(Entity entity) {
        entityModifiers.forEach((c) -> c.accept(entity));
    }
}
