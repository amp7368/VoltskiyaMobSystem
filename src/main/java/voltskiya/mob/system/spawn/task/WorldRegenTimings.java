package voltskiya.mob.system.spawn.task;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    public void mobToTry() {
        mobToTry.mark();
    }

    public void chooseLocation() {
        chooseLocation.mark();
    }

    public void calculateBiomeInfo() {
        calculateBiomeInfo.mark();
    }

    public void checkBiomeSpawnerFails() {
        checkBiomeSpawnerFails.mark();
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

    public String toString() {
        TimingsElement[] timings = {
            mobToTry,
            chooseLocation,
            calculateBiomeInfo,
            checkBiomeSpawnerFails,
            randomMobSpawner,
            shouldSpawn,
            globalRules,
            createMob,
            insertMob};
        return "Timings Report [" + Arrays.stream(timings)
            .map(TimingsElement::toString)
            .collect(Collectors.joining("\n")) + "]";
    }
}
