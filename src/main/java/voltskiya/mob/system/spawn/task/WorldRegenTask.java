package voltskiya.mob.system.spawn.task;

import com.voltskiya.lib.timings.scheduler.CancellingAsyncTask;
import com.voltskiya.lib.timings.scheduler.VoltTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import voltskiya.apple.utilities.chance.weight.ChanceWeightedChoice;
import voltskiya.mob.system.base.biome.BiomeDatabases;
import voltskiya.mob.system.base.biome.BiomeType;
import voltskiya.mob.system.base.mob.MobTypeSpawnerInBiome;
import voltskiya.mob.system.base.selector.ExtendsMob;
import voltskiya.mob.system.base.spawner.BuiltSpawner;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.world.mob.ShouldSpawningResult;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.spawn.config.MapRegenConfig;
import voltskiya.mob.system.spawn.config.RegenConfig;
import voltskiya.mob.system.spawn.config.RegenStatsMap;
import voltskiya.mob.system.spawn.util.CollisionRule;
import voltskiya.mob.system.storage.mob.DStoredMob;
import voltskiya.mob.system.storage.mob.MobStorage;

public class WorldRegenTask implements Runnable {

    private final Runnable callback;
    private final ChanceWeightedChoice<MobTypeSpawnerInBiome> random = new ChanceWeightedChoice<>(
        spawner -> spawner.extendsMob().spawnWeight);
    private Location locationToTry;
    private BiomeType biomeType;
    private BuiltSpawner biomeSpawner;
    private boolean isDone = false;
    private WorldRegenTimings timings = new WorldRegenTimings();

    public WorldRegenTask(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            this.doTask();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            callback.run();
        }
    }

    private void doTask() {
        timings.mobToTry();
        MobTypeSpawnerInBiome mobToTry = chooseMobToTry();
        timings.mobToTry();
        if (mobToTry == null) return;

        timings.shouldSpawn();
        SpawningContext spawnContext = SpawningContext.create(locationToTry);
        ShouldSpawningResult shouldSpawn = mobToTry.spawner().shouldSpawn(spawnContext);
        if (!shouldSpawn.canFutureSpawn()) return;
        timings.shouldSpawn();

        timings.globalRules();
        this.globalRules(mobToTry.mob().getEntity().getEntityType(), spawnContext);
        timings.globalRules();
        if (isDone()) return;

        timings.createMob();
        DStoredMob spawnedMob = new DStoredMob(mobToTry.mob().getId(), locationToTry);
        spawnedMob.setSpawnDelay(shouldSpawn.getSpawnDelay());
        logMobSpawn(spawnedMob);
        timings.createMob();

        timings.insertMob();
        CancellingAsyncTask task = VoltTask.cancelingAsyncTask(() -> MobStorage.insertMobs(List.of(spawnedMob)));
        task.start(ModuleSpawning.get().getTaskManager());
        timings.insertMob();

//        timings.report();
    }

    private void globalRules(EntityType bukkitType, SpawningContext spawnContext) {
        if (bukkitType == null) {
            setDone();
            return;
        }
        Optional<net.minecraft.world.entity.EntityType<?>> type = net.minecraft.world.entity.EntityType.byString(
            bukkitType.getKey().asString());
        if (type.isEmpty()) {
            setDone();
            return;
        }
        Location validLocation = CollisionRule.calculateValidSpawn(type.get(), spawnContext.location());
        if (validLocation == null) {
            setDone();
            return;
        }
        spawnContext.setLocation(validLocation);
    }

    private void logMobSpawn(DStoredMob spawnedMob) {
        if (!RegenConfig.get().logMobSpawn) return;
        ModuleSpawning.stats.createMob();
        Location loc = spawnedMob.getLocation();
        String mobName = spawnedMob.getMobType().getName();
        String worldName = loc.getWorld().getName();
        String message = String.format("create mob %s at %s %d %d %d", mobName, worldName, loc.getBlockX(), loc.getBlockY(),
            loc.getBlockZ());
        ModuleSpawning.get().logger().info(message);
    }

    @Nullable
    public MobTypeSpawnerInBiome chooseMobToTry() {
        timings.chooseLocation();
        chooseLocation();
        timings.chooseLocation();
        if (isDone()) return null;

        timings.calculateBiomeInfo();
        calculateBiomeInfo();
        timings.calculateBiomeInfo();
        if (isDone()) return null;

        timings.checkBiomeSpawnerFails();
        checkBiomeSpawnerFails();
        timings.checkBiomeSpawnerFails();
        if (isDone()) return null;

        timings.randomMobSpawner();
        MobTypeSpawnerInBiome spawner = randomMobSpawner();
        timings.randomMobSpawner();
        return spawner;
    }

    @Nullable
    private MobTypeSpawnerInBiome randomMobSpawner() {
        List<ExtendsMob> mobsInBiome = biomeSpawner.getExtendsMob();
        if (mobsInBiome.isEmpty()) return null;

        List<MobTypeSpawnerInBiome> spawners = new ArrayList<>();
        for (ExtendsMob extendsMob : mobsInBiome) {
            MobTypeSpawnerInBiome mobSpawner = extendsMob.mob.mapped().getBiomeSpawner(extendsMob, biomeSpawner);

            spawners.add(mobSpawner);
        }
        return this.random.choose(spawners);
    }

    private void calculateBiomeInfo() {
        Biome mcBiome = this.locationToTry.getBlock().getBiome();
        biomeType = BiomeDatabases.getBiomeType().getFromMC(mcBiome.getKey());
        if (biomeType == null) {
            setDone();
            return;
        }
        biomeSpawner = biomeType.getSpawner();
    }

    private void chooseLocation() {
        MapRegenConfig map = RegenStatsMap.chooseMap();
        locationToTry = map.randomGroundLocation(timings);
        if (locationToTry == null) setDone();
    }

    private void checkBiomeSpawnerFails() {
        double globalSpawnRate = BiomeDatabases.getBiomeType().getGlobalSpawnRate();
        double spawnRate = biomeSpawner.attributesTopLevel().getNormalizedSpawnRate(globalSpawnRate);
        if (spawnRate > this.random.random().nextDouble())
            setDone();
    }

    private void setDone() {
        this.isDone = true;
    }

    private boolean isDone() {
        return isDone;
    }

}
