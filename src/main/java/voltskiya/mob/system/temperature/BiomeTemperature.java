package voltskiya.mob.system.temperature;

import java.util.Map;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.spawning.conditions.TimeOfDay;

public class BiomeTemperature {

    private BiomeUUID uuid;
    private Map<TimeOfDay, WeatherConditions> dailyTemperatures;

}
