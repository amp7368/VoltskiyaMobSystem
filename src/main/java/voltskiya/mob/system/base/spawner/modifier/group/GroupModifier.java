package voltskiya.mob.system.base.spawner.modifier.group;

import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
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
        Block block = context.feetBlock();
        Entity originalSpawned = result.getSpawned();
        NBTEntity originalNbt = new NBTEntity(originalSpawned);
        // todo does this work
        originalNbt.removeKey("UUID");
        int mobCount = config.choose().getCount();
        for (int i = 0; i < mobCount; i++) {
            World world = block.getWorld();
            Location location = block.getLocation();
            EntityType entityType = originalSpawned.getType();
            world.spawnEntity(location, entityType, SpawnReason.NATURAL,
                copy -> new NBTEntity(copy).mergeCompound(originalNbt));
        }
    }

    @Override
    public SpawningModifierPriority getModifyPriority() {
        return SpawningModifierPriority.LAST;
    }
}
