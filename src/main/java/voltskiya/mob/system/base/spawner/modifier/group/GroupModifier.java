package voltskiya.mob.system.base.spawner.modifier.group;

import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.mob.MobType;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifier;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifierPriority;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class GroupModifier extends SpawningModifier<GroupModifierFactory> {

    public GroupModifier(GroupModifierFactory config, SpawningContext context, SpawnerSummonResult result) {
        super(config, context, result);
    }


    @Override
    public void preModifyEntity() {
    }

    @Override
    public void modifyEntity() {
        MobType mobType = result.getMobType();
        int mobCount = config.choose().getCount();
        VoltskiyaPlugin.get().scheduleSyncDelayedTask(() -> {
            for (int i = 1; i < mobCount; i++) {
                result.spawn(mobType, (modifier) -> !(modifier instanceof GroupModifier));
            }
        });
    }

    @Override
    public SpawningModifierPriority getModifyPriority() {
        return SpawningModifierPriority.LAST;
    }
}
