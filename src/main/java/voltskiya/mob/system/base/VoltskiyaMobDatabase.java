package voltskiya.mob.system.base;

import com.voltskiya.lib.pmc.FileIOServiceNow;
import apple.utilities.database.ajd.AppleAJD;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import java.io.File;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.VoltskiyaPlugin;
import voltskiya.mob.system.base.storage.mob.StoredMob;

public class VoltskiyaMobDatabase {

    public static void load() {
        VoltskiyaMobDatabaseConfig loadedConfig = loadConfig();
        DataSourceConfig dataSourceConfig = configureDataSource(loadedConfig);
        DatabaseConfig dbConfig = configureDatabase(dataSourceConfig);
        DatabaseFactory.createWithContextClassLoader(dbConfig,
            VoltskiyaPlugin.class.getClassLoader());

    }

    @NotNull
    private static DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSourceConfig(dataSourceConfig);
        dbConfig.setAutoPersistUpdates(true);
        dbConfig.setDdlGenerate(true);
        dbConfig.setDdlRun(true);
        dbConfig.addAll(List.of(StoredMob.class));
        return dbConfig;
    }

    @NotNull
    private static DataSourceConfig configureDataSource(VoltskiyaMobDatabaseConfig loadedConfig) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername(loadedConfig.getUsername());
        dataSourceConfig.setPassword(loadedConfig.getPassword());
        dataSourceConfig.setUrl(loadedConfig.getUrl());
        return dataSourceConfig;
    }

    private static VoltskiyaMobDatabaseConfig loadConfig() {
        File file = ModuleBase.get().getFile("DatabaseConfig.json");
        return AppleAJD.createInst(VoltskiyaMobDatabaseConfig.class, file,
            FileIOServiceNow.taskCreator()).loadOrMake();
    }
}
