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
import voltskiya.mob.system.base.spawner.LeafSpawner;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.player.region.RegionUtil;
import voltskiya.mob.system.player.world.mob.ShouldSpawningResult;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.spawn.config.RegenConfig;
import voltskiya.mob.system.spawn.config.RegenStatsMap;
import voltskiya.mob.system.spawn.util.CollisionRule;
import voltskiya.mob.system.storage.mob.DStoredMob;
import voltskiya.mob.system.storage.mob.MobStorage;

public class WorldRegenTask implements Runnable {

    private static final int MOB_SPAWNERS_TO_TRY = 15;
    private static final int LOCAL_SPAWN_RATE_RADIUS = 150;
    private static final int LOCAL_SPAWN_RATE_Y_RADIUS = 35;
    private static final double LOCAL_SPAWN_RATE_BLOCKS =
        LOCAL_SPAWN_RATE_RADIUS * LOCAL_SPAWN_RATE_RADIUS * LOCAL_SPAWN_RATE_Y_RADIUS * Math.pow(2, 3);
    private static final double LOCAL_DENSITY_MULTIPLIER = 1 / 2d;
    private final Runnable callback;
    private final ChanceWeightedChoice<MobTypeSpawnerInBiome> random = new ChanceWeightedChoice<>(MobTypeSpawnerInBiome::getSpawnRate);
    private final WorldRegenTimings timings =
        RegenConfig.get().shouldDoTimings() ? new WorldRegenTimings() : new WorldRegenPassThroughTimings();
    private Location locationToTry;
    private BiomeType biomeType;
    private BuiltSpawner biomeSpawner;
    private boolean isDone = false;
    private RegenStatsMap map;

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
        List<MobTypeSpawnerInBiome> mobsToTry = chooseMobToTry();
        timings.mobToTry();
        if (mobsToTry == null) return;
        for (MobTypeSpawnerInBiome mobToTry : mobsToTry) {
            timings.shouldSpawn();
            SpawningContext spawnContext = SpawningContext.create(locationToTry);
            ShouldSpawningResult shouldSpawn = mobToTry.spawner().shouldSpawn(spawnContext);
            if (!shouldSpawn.canFutureSpawn()) continue;
            timings.shouldSpawn();

            timings.globalRules();
            this.globalRules(mobToTry.mob().getEntity().getEntityType(), spawnContext);
            timings.globalRules();
            if (isDone()) continue;
            timings.createMob();
            DStoredMob spawnedMob = new DStoredMob(mobToTry.mob().getId(), spawnContext.location());
            spawnedMob.setSpawnDelay(shouldSpawn.getSpawnDelay());
            logMobSpawn(spawnedMob);
            timings.createMob();

            timings.insertMob();
            CancellingAsyncTask task = VoltTask.cancelingAsyncTask(() -> MobStorage.insertMobs(List.of(spawnedMob)));
            task.start(ModuleSpawning.get().getTaskManager());
            timings.insertMob();
            timings.report();
            return;
        }
        timings.report();
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
    public List<MobTypeSpawnerInBiome> chooseMobToTry() {
        timings.chooseLocation();
        chooseLocation();
        timings.chooseLocation();
        if (isDone()) return null;

        timings.calculateBiomeInfo();
        calculateBiomeInfo();
        timings.calculateBiomeInfo();
        if (isDone()) return null;

        timings.localSpawnRate();
        localSpawnRate();
        timings.localSpawnRate();
        if (isDone()) return null;

        timings.randomMobSpawner();
        List<MobTypeSpawnerInBiome> mobsToTry = randomMobSpawner();
        timings.randomMobSpawner();
        return mobsToTry;
    }

    private void localSpawnRate() {
        int mobCount = MobStorage.countMobs(this.locationToTry, LOCAL_SPAWN_RATE_RADIUS, LOCAL_SPAWN_RATE_Y_RADIUS);
        double density = mobCount / LOCAL_SPAWN_RATE_BLOCKS;
        double spawnRate = BiomeDatabases.getBiomeType().normalizedSpawnRate(biomeSpawner.getMaxSpawnRate());

        boolean isTooHigh = map.isDensityTooHigh(density / spawnRate * LOCAL_DENSITY_MULTIPLIER);
        if (isTooHigh) setDone();
    }

    private List<MobTypeSpawnerInBiome> randomMobSpawner() {
        List<MobTypeSpawnerInBiome> spawners = new ArrayList<>();
        for (LeafSpawner leaf : biomeSpawner.getLeaves()) {
            List<ExtendsMob> mobsInBiome = leaf.getExtendsMob();
            for (ExtendsMob extendsMob : mobsInBiome) {
                MobTypeSpawnerInBiome mobSpawner = extendsMob.mob.mapped().getBiomeSpawner(extendsMob, leaf);
                spawners.add(mobSpawner);
            }
        }
        if (spawners.isEmpty()) {
            BiomeErrors.get().add(biomeType.getMinecraft().getKey());
            return spawners;
        }
        List<MobTypeSpawnerInBiome> mobsToTry = new ArrayList<>();
        for (int i = 0; i < MOB_SPAWNERS_TO_TRY; i++) {
            mobsToTry.add(this.random.choose(spawners));
        }
        return mobsToTry;
    }

    private void calculateBiomeInfo() {
        Biome mcBiome = this.locationToTry.getBlock().getBiome();
        biomeType = BiomeDatabases.getBiomeType().getFromMC(mcBiome.getKey());
        if (biomeType == null) {
            BiomeErrors.get().add(mcBiome.getKey().toString());
            setDone();
            return;
        }
        biomeSpawner = biomeType.getSpawner();
    }

    private void chooseLocation() {
        map = RegenStatsMap.chooseMap();
        if (map == null) {
            setDone();
            return;
        }
        locationToTry = map.config.randomGroundLocation(timings);
        if (locationToTry == null) {
            setDone();
            return;
        }
        if (RegionUtil.isInRegion(locationToTry)) setDone();
    }


    private void setDone() {
        this.isDone = true;
    }

    private boolean isDone() {
        return isDone;
    }

}
