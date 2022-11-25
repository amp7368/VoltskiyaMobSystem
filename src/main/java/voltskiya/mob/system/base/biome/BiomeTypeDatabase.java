package voltskiya.mob.system.base.biome;

import apple.mc.utilities.data.serialize.GsonSerializeMC;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.base.ModuleBase;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.base.spawner.BiomeSpawner;
import voltskiya.mob.system.temperature.BiomeTemperature;
import voltskiya.mob.system.temperature.ModuleTemperature;

public class BiomeTypeDatabase {

    private static AppleAJDInst<BiomeSpawningDatabase> spawningManager;
    private static AppleAJDInst<BiomeTemperatureDatabase> temperatureManager;
    private static AppleAJDInst<BiomeTypeDatabase> baseManager;

    private final Map<BiomeUUID, BiomeBase> biomes = new HashMap<>();
    private final Map<NamespacedKey, BiomeUUID> minecraftToBiome = new HashMap<>();
    private final Map<String, BiomeUUID> nameToBiome = new HashMap<>();

    public static void load() {
        spawningManager = AppleAJD.createInst(BiomeSpawningDatabase.class,
            ModuleSpawning.get().getFile("BiomesSpawning.json"), FileIOServiceNow.taskCreator());
        temperatureManager = AppleAJD.createInst(BiomeTemperatureDatabase.class,
            ModuleTemperature.get().getFile("BiomesTemperature.json"),
            FileIOServiceNow.taskCreator());
        baseManager = AppleAJD.createInst(BiomeTypeDatabase.class,
            ModuleBase.get().getFile("Biomes.json"), FileIOServiceNow.taskCreator());
        spawningManager.setSerializingJson(GsonSerializeMC.completeGsonBuilderMC().create());
        temperatureManager.setSerializingJson(GsonSerializeMC.completeGsonBuilderMC().create());
        baseManager.setSerializingJson(GsonSerializeMC.completeGsonBuilderMC().create());
        spawningManager.loadOrMake();
        temperatureManager.loadOrMake();
        baseManager.loadOrMake();
    }

    public static BiomeTypeDatabase getBase() {
        return baseManager.getInstance();
    }

    public static BiomeSpawningDatabase getSpawning() {
        return spawningManager.getInstance();
    }

    public static BiomeTemperatureDatabase getTemperature() {
        return temperatureManager.getInstance();
    }

    public static BiomeUUID getBiomeUUID(NamespacedKey key) {
        BiomeTypeDatabase base = getBase();
        synchronized (base.minecraftToBiome) {
            return base.getBiomeType(base.minecraftToBiome.get(key)).getUUID();
        }
    }

    public BiomeUUID getBiomeUUID(String name) {
        synchronized (this.nameToBiome) {
            return this.getBiomeType(this.nameToBiome.get(name)).getUUID();
        }
    }

    private BiomeBase getBiomeType(BiomeUUID uuid) {
        return this.biomes.get(uuid);
    }

    public static class BiomeSpawningDatabase {

        private final Map<BiomeUUID, BiomeSpawner> biomes = new HashMap<>();

        @Nullable
        public BiomeSpawner get(@Nullable BiomeUUID uuid) {
            if (uuid == null)
                return null;
            synchronized (this.biomes) {
                return this.biomes.get(uuid);
            }
        }
    }

    public static class BiomeTemperatureDatabase {

        private final Map<BiomeUUID, BiomeTemperature> biomes = new HashMap<>();

        @Nullable
        public BiomeTemperature get(@Nullable BiomeUUID uuid) {
            if (uuid == null)
                return null;
            synchronized (this.biomes) {
                return this.biomes.get(uuid);
            }
        }
    }
}
