package voltskiya.mob.system.base;

import apple.lib.pmc.FileIOServiceNow;
import apple.utilities.database.ajd.AppleAJD;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import java.io.File;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import voltskiya.mob.system.spawning.storage.StoredMob;

public class VoltskiyaMobDatabase {

    public static void load() {
        VoltskiyaMobDatabaseConfig loadedConfig = loadConfig();

        DataSourceConfig dataSourceConfig = configureDataSource(loadedConfig);
        DatabaseConfig dbConfig = configureDatabase(dataSourceConfig);

        dbConfig.addAll(List.of(StoredMob.class));
        DatabaseFactory.create(dbConfig);
    }

    @NotNull
    private static DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSourceConfig(dataSourceConfig);
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
