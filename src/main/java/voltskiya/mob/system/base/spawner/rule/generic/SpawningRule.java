package voltskiya.mob.system.base.spawner.rule.generic;

import voltskiya.mob.system.base.spawner.context.SpawningContext;

public abstract class SpawningRule {
    public String typeId;
    public SpawningRule(String typeId){
        this.typeId = typeId;
    }
    public abstract boolean isBreaksRule(SpawningContext context);

}
