package voltskiya.mob.system.base.spawner.modifier.group;

public class GroupModifierChance {

    public static final GroupModifierChance DEFAULT = new GroupModifierChance(1);
    private int count;

    public GroupModifierChance(int count) {
        this.count = count;
    }

    public GroupModifierChance() {
    }

    public int getCount() {
        return count;
    }
}
