package voltskiya.mob.system.base.biome;


public class BiomeBase {

    private BiomeUUID uuid = new BiomeUUID();
//    private ResourceLocation minecraftKey;
    private String name;

//    public BiomeBase(ResourceLocation minecraftKey, String name) {
//        this.minecraftKey = minecraftKey;
//        this.name = name;
//    }

    public BiomeBase() {
    }

    public BiomeUUID getUUID() {
        return this.uuid;
    }
}
