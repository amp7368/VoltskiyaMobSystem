package voltskiya.mob.system.base.spawner.context;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.base.spawner.modifier.CreateSpawningModifier;
import voltskiya.mob.system.base.spawner.rule.temporal.TimeOfDay;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;
import voltskiya.mob.system.storage.mob.DStoredMob;

public final class SpawningContext {

    private final BiomeUUID biomeUUID;
    private final Block belowFeetBlock;
    private final List<CreateSpawningModifier> modifyResult = new ArrayList<>();
    private Block feetBlock;
    private Location location;

    public SpawningContext(Location location, BiomeUUID biomeUUID, Block feetBlock, Block belowFeetBlock) {
        this.location = location;
        this.biomeUUID = biomeUUID;
        this.feetBlock = feetBlock;
        this.belowFeetBlock = belowFeetBlock;
    }

    public static SpawningContext create(DStoredMob storedMob) {
        return create(storedMob.getLocation());
    }

    @NotNull
    public static SpawningContext create(Location location) {
        Block feetBlock = location.getBlock();
        Block belowFeetBlock = location.clone().subtract(0, 1, 0).getBlock();
        BiomeUUID biomeUUID = BiomeDatabases.getBiome(feetBlock.getBiome().getKey());
        return new SpawningContext(location, biomeUUID, feetBlock, belowFeetBlock);
    }

    public void modifyResult(CreateSpawningModifier modifier) {
        modifyResult.add(modifier);
    }

    public TimeOfDay timeOfDay() {
        return TimeOfDay.getTime(time());
    }

    public long time() {
        return this.location().getWorld().getTime();
    }

    public void setLocation(Location location) {
        this.location = location;
        this.feetBlock = location.clone().subtract(0, 1, 0).getBlock();
    }

    public Location location() {
        return location;
    }

    public BiomeUUID biomeUUID() {
        return biomeUUID;
    }

    public Block feetBlock() {
        return feetBlock;
    }

    public Block belowFeetBlock() {
        return belowFeetBlock;
    }

    public void applyModifications(SpawnerSummonResult result) {
        modifyResult.forEach(modifier -> modifier.createModifier(this, result));
    }
}
