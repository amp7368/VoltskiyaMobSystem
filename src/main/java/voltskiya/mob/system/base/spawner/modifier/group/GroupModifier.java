package voltskiya.mob.system.base.spawner.modifier.group;

import java.util.ArrayList;
import java.util.List;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifier;
import voltskiya.mob.system.base.spawner.context.SpawningContext;

public class GroupModifier extends SpawningModifier {

    private List<GroupSpawn> groups = new ArrayList<>();

    public GroupModifier(SpawningContext context) {
        super(context);
    }

    public void merge(GroupModifier overrideWith) {
        this.groups.addAll(overrideWith.groups);
    }
}
