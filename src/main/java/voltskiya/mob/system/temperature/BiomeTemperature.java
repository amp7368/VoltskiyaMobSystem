package voltskiya.mob.system.temperature;

import java.util.EnumMap;
import voltskiya.mob.system.base.biome.BiomeUUID;
import voltskiya.mob.system.spawning.rule.TimeOfDay;

public class BiomeTemperature {

    private BiomeUUID uuid;
    private EnumMap<TimeOfDay, WeatherConditions> dailyTemperatures;

}
