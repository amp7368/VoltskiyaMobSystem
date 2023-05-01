package voltskiya.mob.system.storage;

import apple.utilities.database.ajd.AppleAJD;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.storage.mob.DStoredMob;
import voltskiya.mob.system.storage.mob.MobStorage;
import voltskiya.mob.system.storage.mob.MobUUIDConverter;
import voltskiya.mob.system.storage.mob.StoredLocation;
import voltskiya.mob.system.storage.world.StoredWorld;

public class VoltskiyaDatabase {

    public static void load() {
        DataSourceConfig dataSourceConfig = configureDataSource();
        DatabaseConfig dbConfig = configureDatabase(dataSourceConfig);

        // We should use the classloader that loaded this plugin
        // because this plugin has our ebean dependencies
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader pluginClassLoader = VoltskiyaPlugin.class.getClassLoader();

        // create the DatabaseFactory with the classloader containing ebean dependencies
        DatabaseFactory.createWithContextClassLoader(dbConfig, pluginClassLoader);

        // Set the current thread's contextClassLoader to the classLoader with the ebean dependencies
        // This allows the class to initialize itself with access to the required class dependencies
        Thread.currentThread().setContextClassLoader(pluginClassLoader);

        // invoke the static initialization of every class that contains a querybean.
        // Note that any method in the class will initialize the class.
        for (Class<?> clazz : List.of(MobStorage.class)) {
            try {
                clazz.getConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        // Restore the contextClassLoader to what it was originally
        Thread.currentThread().setContextClassLoader(originalClassLoader);

        VoltskiyaPlugin.get().getLogger().info("Successfully created database");
    }

    @NotNull
    private static DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSourceConfig(dataSourceConfig);
        dbConfig.setAutoPersistUpdates(true);
        dbConfig.setDdlGenerate(true);
        dbConfig.setDdlRun(false);
        dbConfig.addAll(getEntities());
        return dbConfig;
    }

    private static List<Class<?>> getEntities() {
        List<Class<?>> entities = new ArrayList<>();
        entities.add(MobUUIDConverter.class);
        entities.addAll(List.of(DStoredMob.class, StoredLocation.class, StoredWorld.class));
        return entities;
    }

    @NotNull
    private static DataSourceConfig configureDataSource() {
        VoltskiyaDatabaseConfig loadedConfig = loadConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername(loadedConfig.getUsername());
        dataSourceConfig.setPassword(loadedConfig.getPassword());
        dataSourceConfig.setUrl(loadedConfig.getUrl());
        return dataSourceConfig;
    }

    private static VoltskiyaDatabaseConfig loadConfig() {
        File file = ModuleStorage.get().getFile("DatabaseConfig.json");
        return AppleAJD.createInst(VoltskiyaDatabaseConfig.class, file,
            FileIOServiceNow.taskCreator()).loadOrMake();
    }
}
