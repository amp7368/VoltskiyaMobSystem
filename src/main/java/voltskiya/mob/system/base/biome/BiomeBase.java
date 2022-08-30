package voltskiya.mob.system.base.biome;

import net.minecraft.resources.ResourceLocation;

public class BiomeBase {

    private BiomeUUID uuid = new BiomeUUID();
    private ResourceLocation minecraftKey;
    private String name;

    public BiomeBase(ResourceLocation minecraftKey, String name) {
        this.minecraftKey = minecraftKey;
        this.name = name;
    }

    public BiomeBase() {
    }
}
