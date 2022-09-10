package voltskiya.mob.system.spawning.spawner;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.base.mob.MobUUID;

public class BiomeSpawning {

    private BiomeUUID uuid;
    private Map<MobUUID, MobTypeSpawner> mobs;
    private float spawnRate;

    @Nullable
    public MobTypeSpawner getSpawner(MobUUID mobUUID) {
        return mobs.get(mobUUID);
    }

    public float getSpawnRate() {
        return spawnRate;
    }
}
