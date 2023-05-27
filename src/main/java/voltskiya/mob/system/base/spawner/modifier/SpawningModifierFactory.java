package voltskiya.mob.system.base.spawner.modifier;

public abstract class SpawningModifierFactory implements CreateSpawningModifier {

    public String typeId;

    protected SpawningModifierFactory(String typeId) {
        this.typeId = typeId;
    }

}
