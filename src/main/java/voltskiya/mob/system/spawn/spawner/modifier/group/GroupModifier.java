package voltskiya.mob.system.spawn.spawner.modifier.group;

import java.util.ArrayList;
import java.util.List;
import voltskiya.mob.system.spawn.spawner.context.SpawningContext;
import voltskiya.mob.system.spawn.spawner.modifier.SpawningModifier;

public class GroupModifier extends SpawningModifier {

    private List<GroupSpawn> groups = new ArrayList<>();

    public GroupModifier(SpawningContext context) {
        super(context);
    }

    public void merge(GroupModifier overrideWith) {
        this.groups.addAll(overrideWith.groups);
    }
}
