package voltskiya.mob.system.spawning.rule.old.group;

import java.util.ArrayList;
import java.util.List;

public class GroupRules {

    private List<GroupSpawn> groups = new ArrayList<>();

    public void merge(GroupRules overrideWith) {
        this.groups.addAll(overrideWith.groups);
    }
}
