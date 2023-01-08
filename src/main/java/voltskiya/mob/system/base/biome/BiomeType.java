package voltskiya.mob.system.base.biome;

import apple.nms.decoding.world.DecodeBiome;
import apple.nms.decoding.world.DecodeMinecraftKey;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;
import voltskiya.mob.system.base.spawner.BuiltSpawner;

public class BiomeType {

    public BiomeUUID id;
    public NamespacedKey minecraft;

    private SpawnSelectorGrouping spawnTags = new SpawnSelectorGrouping();

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
}
