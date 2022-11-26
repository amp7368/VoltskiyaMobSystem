package voltskiya.mob.system.base.spawner;

import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.base.mob.MobUUID;

public class BiomeSpawner {

    private BiomeUUID biomeId;
    private Map<MobUUID, MobSpawnerFragment> mobs;
    private float spawnRate;

    @Nullable
    public MobSpawnerFragment getSpawner(MobUUID mobUUID) {
        return mobs.get(mobUUID);
    }

    public Collection<MobSpawnerFragment> getAllSpawners() {
        return this.mobs.values();
    }

    public float getSpawnRate() {
        return spawnRate;
    }

}
