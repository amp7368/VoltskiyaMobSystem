package voltskiya.mob.system.base.spawner.modifier.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.modifier.GsonMapSpawningModifier;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifierFactory;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class GroupModifierFactory extends SpawningModifierFactory {

    private transient final Random random = new Random();
    protected List<GroupModifierChance> groups = new ArrayList<>();

    public GroupModifierFactory() {
        super(GsonMapSpawningModifier.GROUP.getTypeId());
    }

    @Override
    public void createModifier(SpawningContext context, SpawnerSummonResult result) {
        new GroupModifier(this, context, result);
    }

    public synchronized GroupModifierChance choose() {
        if (groups.isEmpty()) return GroupModifierChance.DEFAULT;
        int chosenIndex = random.nextInt(groups.size());
        return groups.get(chosenIndex);
    }
}
