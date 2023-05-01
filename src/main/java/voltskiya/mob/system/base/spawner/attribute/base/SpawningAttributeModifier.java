package voltskiya.mob.system.base.spawner.attribute.base;

public abstract class SpawningAttributeModifier {

    private final String typeId;

    public SpawningAttributeModifier(String typeId) {
        this.typeId = typeId;
    }

    public abstract void modify(SpawningComputedAttributes original);

    public SpawningAttributePriority getPriority() {
        return SpawningAttributePriority.NORMAL;
    }
}
