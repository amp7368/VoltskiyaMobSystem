package voltskiya.mob.system.base.biome;

import apple.nms.decoding.world.DecodeBiome;
import apple.nms.decoding.world.DecodeMinecraftKey;
import java.util.Set;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;
import voltskiya.mob.system.base.spawner.BuiltSpawner;

public class BiomeType {

    private final SpawnSelectorGrouping spawnTags = new SpawnSelectorGrouping();
    public BiomeUUID id;
    public NamespacedKey minecraft;
    private double globalSpawnRate = 0;

    public BiomeType(NamespacedKey minecraft) {
        this.id = BiomeUUID.random();
        this.minecraft = minecraft;
    }

    public BiomeType() {
    }

    public Biome getMinecraft(World world) {
        return DecodeBiome.getBiomeFromKey(world, DecodeMinecraftKey.makeKey(this.minecraft));
    }

    public BuiltSpawner getSpawner() {
        return spawnTags.compiled();
    }

    public void loadGlobalStats() {
        Set<MobUUID> mobsInBiome = spawnTags.compiled().getExtendedByMob();
        for (MobUUID mob : mobsInBiome) {
            BuiltSpawner mobSpawner = mob.mapped().getRawSpawner();
            globalSpawnRate += mobSpawner.attributesTopLevel().getSpawnRate();
        }
        if (globalSpawnRate == 0) globalSpawnRate = 1;
    }

    public double getGlobalSpawnRate() {
        return globalSpawnRate;
    }

}
