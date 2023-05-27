package voltskiya.mob.system.base.biome;

import apple.mc.utilities.data.serialize.GsonSerializeMC;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.base.biome.database.BiomeTemperatureDatabase;
import voltskiya.mob.system.base.biome.database.BiomeTypeDatabase;
import voltskiya.mob.system.spawn.task.BiomeErrors;

public class BiomeDatabases {

    private static AppleAJDInst<BiomeTemperatureDatabase> temperatureManager;
    private static AppleAJDInst<BiomeDatabases> idManager;
    private static AppleAJDInst<BiomeTypeDatabase> biomeTypeManager;
    private transient final Object minecraftToBiomeSync = new Object();
    protected Map<NamespacedKey, BiomeUUID> minecraftToBiome = new HashMap<>();

    public static void load() {
        File idsFile = ModuleBase.get().getFile("BiomeIds.json");
        File tempsFile = ModuleBase.get().getFile("BiomeTemperatures.json");
        File typesFile = ModuleBase.get().getFile("BiomeTypes.json");
        idManager = AppleAJD.createInst(BiomeDatabases.class, idsFile, FileIOServiceNow.taskCreator(), gson());
        temperatureManager = AppleAJD.createInst(BiomeTemperatureDatabase.class, tempsFile, FileIOServiceNow.taskCreator(), gson());
        biomeTypeManager = AppleAJD.createInst(BiomeTypeDatabase.class, typesFile, FileIOServiceNow.taskCreator(), gson());
        idManager.loadOrMake(true);
        temperatureManager.loadOrMake(true);
        biomeTypeManager.loadOrMake(true);
        // make sure all minecraft biomes are saved
        for (Biome biome : Biome.values()) {
            BiomeDatabases.getBiome(biome.getKey());
        }
        Set<String> biomeTypes = new HashSet<>();
        for (BiomeType biome : getBiomeType().list()) {
            String minecraft = biome.getMinecraft().toString();
            if (!biomeTypes.add(minecraft)) {
                BiomeErrors.get().addDuplicateType(minecraft);
            }
        }
        idManager.saveNow();
        temperatureManager.saveNow();
        biomeTypeManager.saveNow();
    }

    @NotNull
    private static Gson gson() {
        GsonBuilder gson = GsonSerializeMC.completeGsonBuilderMC();
        gson.enableComplexMapKeySerialization();
        return BiomeType.gson(gson).create();
    }

    public static BiomeDatabases getBase() {
        return idManager.getInstance();
    }

    public static BiomeTypeDatabase getBiomeType() {
        return biomeTypeManager.getInstance();
    }

    public static BiomeTemperatureDatabase getTemperature() {
        return temperatureManager.getInstance();
    }

    @NotNull
    public static BiomeUUID getBiome(@NotNull NamespacedKey key) {
        return getBase().getBiomeI(key);
    }

    @NotNull
    public BiomeUUID getBiomeI(@NotNull NamespacedKey key) {
        synchronized (this.minecraftToBiomeSync) {
            BiomeUUID biomeUUID = this.minecraftToBiome.get(key);
            if (biomeUUID != null) return biomeUUID;
            BiomeType biomeType = new BiomeType(key);
            this.minecraftToBiome.put(key, biomeType.uuid);
            biomeTypeManager.getInstance().put(biomeType.uuid, biomeType);
            return biomeType.uuid;
        }
    }
}
