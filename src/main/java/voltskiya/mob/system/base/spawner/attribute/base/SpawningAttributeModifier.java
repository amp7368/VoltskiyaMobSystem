package voltskiya.mob.system.base.spawner.attribute.base;

public abstract class SpawningAttributeModifier {

    public abstract void modify(SpawningComputedAttributes original);

    public SpawningAttributePriority getPriority() {
        return SpawningAttributePriority.NORMAL;
    }
}
