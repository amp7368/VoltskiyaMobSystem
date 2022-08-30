package voltskiya.mob.system.spawning.conditions;

import java.util.Map;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.base.mob.MobUUID;

public class BiomeSpawning {

    private BiomeUUID uuid;
    private Map<MobUUID, MobSpawnConditions> mobs;
    private float spawnRate;

}
