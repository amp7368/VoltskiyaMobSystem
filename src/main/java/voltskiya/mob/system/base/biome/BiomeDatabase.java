package voltskiya.mob.system.base.biome;

import apple.lib.pmc.FileIOServiceNow;
import apple.mc.utilities.data.serialize.GsonSerializeMC;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import apple.utilities.threading.service.queue.AsyncTaskQueue;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.spawning.ModuleSpawning;
import voltskiya.mob.system.spawning.conditions.BiomeSpawning;
import voltskiya.mob.system.temperature.BiomeTemperature;
import voltskiya.mob.system.temperature.ModuleTemperature;

public class BiomeDatabase {

    private static AppleAJDInst<BiomeSpawningDatabase, AsyncTaskQueue> spawningManager;
    private static AppleAJDInst<BiomeTemperatureDatabase, AsyncTaskQueue> temperatureManager;
    private static AppleAJDInst<BiomeDatabase, AsyncTaskQueue> baseManager;

    private final Map<BiomeUUID, BiomeBase> biomes = new HashMap<>();
    private final Map<ResourceLocation, BiomeUUID> minecraftToBiome = new HashMap<>();
    private final Map<String, BiomeUUID> nameToBiome = new HashMap<>();

    public static void load() {
        spawningManager = AppleAJD.createInst(BiomeSpawningDatabase.class,
            ModuleSpawning.get().getFile("BiomesSpawning.json"),
            FileIOServiceNow.get().taskCreator());
        temperatureManager = AppleAJD.createInst(BiomeTemperatureDatabase.class,
            ModuleTemperature.get().getFile("BiomesTemperature.json"),
            FileIOServiceNow.get().taskCreator());
        baseManager = AppleAJD.createInst(BiomeDatabase.class,
            ModuleBase.get().getFile("Biomes.json"), FileIOServiceNow.get().taskCreator());
        spawningManager.setSerializingJson(GsonSerializeMC.completeGsonBuilderMC().create());
        temperatureManager.setSerializingJson(GsonSerializeMC.completeGsonBuilderMC().create());
        baseManager.setSerializingJson(GsonSerializeMC.completeGsonBuilderMC().create());
        spawningManager.loadOrMake();
        temperatureManager.loadOrMake();
        baseManager.loadOrMake();
    }

    private static class BiomeSpawningDatabase {

        private final Map<BiomeUUID, BiomeSpawning> biomes = new HashMap<>();
    }

    private static class BiomeTemperatureDatabase {

        private final Map<BiomeUUID, BiomeTemperature> biomes = new HashMap<>();
    }
}
