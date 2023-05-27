package voltskiya.mob.system.spawn.task;

import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.spawn.timings.TimingsElement;

public class WorldRegenTimings {

    private final TimingsElement mobToTry = new TimingsElement("mobToTry");
    private final TimingsElement chooseLocation = new TimingsElement("chooseLocation");
    private final TimingsElement calculateBiomeInfo = new TimingsElement("calculateBiomeInfo");
    private final TimingsElement checkBiomeSpawnerFails = new TimingsElement("biomeSpawnerFails");
    private final TimingsElement randomMobSpawner = new TimingsElement("randomMobSpawner");
    private final TimingsElement shouldSpawn = new TimingsElement("shouldSpawn");
    private final TimingsElement globalRules = new TimingsElement("globalRules");
    private final TimingsElement createMob = new TimingsElement("createMob");
    private final TimingsElement insertMob = new TimingsElement("insertMob");
    private final TimingsElement chunkLoad = new TimingsElement("chunkLoad");
    private final TimingsElement chunkScan = new TimingsElement("chunkScan");
    private final TimingsElement localSpawnRate = new TimingsElement("localSpawnRate");

    public void mobToTry() {
        mobToTry.mark();
    }

    public void chooseLocation() {
        chooseLocation.mark();
    }

    public void calculateBiomeInfo() {
        calculateBiomeInfo.mark();
    }

    public void randomMobSpawner() {
        randomMobSpawner.mark();
    }

    public void shouldSpawn() {
        shouldSpawn.mark();
    }

    public void globalRules() {
        globalRules.mark();
    }

    public void createMob() {
        createMob.mark();
    }

    public void insertMob() {
        insertMob.mark();
    }

    public void chunkLoad() {
        chunkLoad.mark();
    }

    public void chunkScan() {
        chunkScan.mark();
    }

    public String toString() {
        TimingsElement[] timings = {
            mobToTry,
            chooseLocation,
            chunkLoad,
            chunkScan,
            calculateBiomeInfo,
            checkBiomeSpawnerFails,
            randomMobSpawner,
            shouldSpawn,
            globalRules,
            createMob,
            insertMob};
        return TimingsElement.report(timings);
    }

    public void report() {
        ModuleSpawning.get().logger().info(toString());
    }


    public void localSpawnRate() {
        localSpawnRate.mark();
    }
}
