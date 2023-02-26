package voltskiya.mob.system.base.biome;

import apple.mc.utilities.data.serialize.GsonSerializeMC;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.google.gson.Gson;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.base.biome.database.BiomeTemperatureDatabase;
import voltskiya.mob.system.base.biome.database.BiomeTypeDatabase;

public class BiomeDatabases {

    private static AppleAJDInst<BiomeTemperatureDatabase> temperatureManager;
    private static AppleAJDInst<BiomeDatabases> idManager;
    private static AppleAJDInst<BiomeTypeDatabase> biomeTypeManager;
    private final Map<NamespacedKey, BiomeUUID> minecraftToBiome = new HashMap<>();

    public static void load() {
        File tempsFile = ModuleBase.get().getFile("BiomeTemperatures.json");
        File typesFile = ModuleBase.get().getFile("BiomeTypes.json");
        File idsFile = ModuleBase.get().getFile("BiomeIds.json");
        idManager = AppleAJD.createInst(BiomeDatabases.class, idsFile, FileIOServiceNow.taskCreator(), gson());
        temperatureManager = AppleAJD.createInst(BiomeTemperatureDatabase.class, tempsFile, FileIOServiceNow.taskCreator(), gson());
        biomeTypeManager = AppleAJD.createInst(BiomeTypeDatabase.class, typesFile, FileIOServiceNow.taskCreator(), gson());
        idManager.loadOrMake();
        temperatureManager.loadOrMake();
        biomeTypeManager.loadOrMake();
        biomeTypeManager.saveNow();
        // make sure all minecraft biomes are saved
        for (Biome biome : Biome.values()) {
            BiomeDatabases.getBiome(biome.getKey());
        }
        idManager.saveNow();
        biomeTypeManager.saveNow();
        getBiomeType().loadGlobalStats();
    }

    @NotNull
    private static Gson gson() {
        return GsonSerializeMC.completeGsonBuilderMC().create();
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
        synchronized (this.minecraftToBiome) {
            BiomeUUID biomeUUID = this.minecraftToBiome.get(key);
            if (biomeUUID != null) return biomeUUID;
            BiomeType biomeType = new BiomeType(key);
            this.minecraftToBiome.put(key, biomeType.id);
            biomeTypeManager.getInstance().put(biomeType.id, biomeType);
            return biomeType.id;
        }
    }
}
