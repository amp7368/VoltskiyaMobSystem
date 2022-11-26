package voltskiya.mob.system.spawn.task;

import com.google.common.util.concurrent.Futures;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.mob.MobTypeDatabase;
import voltskiya.mob.system.base.spawner.BiomeSpawner;
import voltskiya.mob.system.base.spawner.MobSpawner;
import voltskiya.mob.system.base.spawner.MobSpawnerFragment;
import voltskiya.mob.system.spawn.config.MapRegenConfig;
import voltskiya.mob.system.spawn.config.RegenStatsMap;

public class WorldRegen {

    private Random random = new Random();

    public Future<Void> run() {
        MapRegenConfig map = RegenStatsMap.chooseMap();
        Location location = map.randomLocation();
        if (location == null) return Futures.immediateVoidFuture();
        Biome mcBiome = location.getBlock().getBiome();
        @Nullable BiomeSpawner spawner = BiomeDatabases.getSpawning().getFromMC(mcBiome.getKey());
        if (spawner == null || spawnerFails(spawner)) return Futures.immediateVoidFuture();
        Collection<MobSpawnerFragment> biomes = spawner.getAllSpawners();
        List<MobSpawner> spawners = new ArrayList<>();
        for (MobSpawnerFragment biome : biomes) {
            MobSpawnerFragment global = MobTypeDatabase.get().getMobType(biome.getUUID()).getSpawner();
            spawners.add(new MobSpawner(biome, global));
        }
        // todo check spawn for each mob
        return Futures.immediateVoidFuture();
    }

    private boolean spawnerFails(@NotNull BiomeSpawner spawner) {
        return spawner.getSpawnRate() < this.random.nextDouble();
    }
}
