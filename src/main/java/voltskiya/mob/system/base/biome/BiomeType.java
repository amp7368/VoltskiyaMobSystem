package voltskiya.mob.system.base.biome;

import apple.nms.decoding.world.DecodeBiome;
import apple.nms.decoding.world.DecodeMinecraftKey;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

public class BiomeType {

    public BiomeUUID id;
    public NamespacedKey minecraft;

    public BiomeType(NamespacedKey minecraft) {
        this.id = new BiomeUUID();
        this.minecraft = minecraft;
    }

    public BiomeType() {
    }

    public Biome getMinecraft(World world) {
        return DecodeBiome.getBiomeFromKey(world, DecodeMinecraftKey.makeKey(this.minecraft));
    }
}
