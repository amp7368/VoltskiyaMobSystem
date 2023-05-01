package voltskiya.mob.system.base.biome;

import com.google.gson.GsonBuilder;
import org.bukkit.NamespacedKey;
import voltskiya.mob.system.base.selector.SpawnSelectorGrouping;
import voltskiya.mob.system.base.selector.SpawnSelectorUUID;
import voltskiya.mob.system.base.spawner.BuiltSpawner;

public class BiomeType {

    public BiomeUUID uuid;
    protected SpawnSelectorGrouping spawnTags;
    protected String name;
    protected NamespacedKey minecraft;
    private transient double globalSpawnRate = 0;

    public BiomeType(BiomeUUID uuid, SpawnSelectorGrouping spawnTags, String name, NamespacedKey minecraft) {
        this.uuid = uuid;
        this.spawnTags = spawnTags;
        this.name = name;
        this.minecraft = minecraft;
    }

    public BiomeType(NamespacedKey minecraft) {
        this.uuid = BiomeUUID.random();
        this.name = minecraft.asString();
        this.minecraft = minecraft;
        this.spawnTags = new SpawnSelectorGrouping();
    }

    public static GsonBuilder gson(GsonBuilder gson) {
        gson.registerTypeHierarchyAdapter(BiomeUUID.class, BiomeUUID.typeAdapter());
        gson.registerTypeHierarchyAdapter(SpawnSelectorUUID.class, SpawnSelectorUUID.typeAdapter());
        return gson;
    }

//    public Biome getMinecraft(World world) {
//        return DecodeBiome.getBiomeFromKey(world, DecodeMinecraftKey.makeKey(this.minecraft));
//    }

    public String getName() {
        return this.name;
    }

    public BuiltSpawner getSpawner() {
        return spawnTags.compiled();
    }

    public SpawnSelectorGrouping getSpawnerTags() {
        this.spawnTags.setName(this.name, this.uuid);
        return this.spawnTags;
    }
}
