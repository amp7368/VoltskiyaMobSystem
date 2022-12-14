package voltskiya.mob.system.base.spawner.modifier;

import java.util.function.Consumer;
import org.bukkit.entity.Entity;
import voltskiya.mob.system.base.spawner.context.SpawningContext;

public abstract class SpawningModifier implements Consumer<Entity> {

    private SpawningContext context;

    public SpawningModifier(SpawningContext context) {
        this.context = context;
    }

    @Override
    public void accept(Entity entity) {

    }
}
