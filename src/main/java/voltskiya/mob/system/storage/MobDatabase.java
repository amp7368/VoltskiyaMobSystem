package voltskiya.mob.system.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import voltskiya.lib.database.VoltskiyaDatabase;
import voltskiya.lib.database.config.VoltskiyaDatabaseConfig;
import voltskiya.lib.database.config.VoltskiyaMysqlConfig;
import voltskiya.mob.system.storage.mob.DStoredMob;
import voltskiya.mob.system.storage.mob.MobStorage;
import voltskiya.mob.system.storage.mob.MobUUIDConverter;
import voltskiya.mob.system.storage.mob.StoredLocation;
import voltskiya.mob.system.storage.world.StoredWorld;

public class MobDatabase extends VoltskiyaDatabase {

    public static final String NAME = "MobSystem";
    private static MobDatabase instance;

    public MobDatabase() {
        instance = this;
    }

    public static MobDatabase get() {
        return instance;
    }


    @Override
    protected List<Class<?>> getEntities() {
        List<Class<?>> entities = new ArrayList<>();
        entities.add(MobUUIDConverter.class);
        entities.add(BaseEntity.class);
        entities.addAll(List.of(DStoredMob.class, StoredLocation.class, StoredWorld.class));
        return entities;
    }

    @Override
    protected Collection<Class<?>> getQueryBeans() {
        return List.of(MobStorage.class);
    }

    @Override
    protected VoltskiyaDatabaseConfig getConfig() {
        return makeConfig(ModuleStorage.get().getFile("DatabaseConfig.json"), VoltskiyaMysqlConfig.class);
    }

    @Override
    protected boolean isDefault() {
        return true;
    }

    @Override
    protected String getName() {
        return NAME;
    }
}
