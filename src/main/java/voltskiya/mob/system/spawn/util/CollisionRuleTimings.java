package voltskiya.mob.system.spawn.util;

import java.util.ArrayList;
import java.util.List;
import voltskiya.mob.system.spawn.ModuleSpawning;
import voltskiya.mob.system.spawn.timings.TimingsElement;

public class CollisionRuleTimings {

    private final TimingsElement gatherWorld = new TimingsElement("gatherWorld");
    private final List<TimingsElement> stepMath = new ArrayList<>();
    private final TimingsElement optimizeWorld = new TimingsElement("optimizeWorld");

    public String toString() {
        List<TimingsElement> timings = new ArrayList<>();
        timings.add(gatherWorld);
        timings.add(optimizeWorld);
        timings.addAll(stepMath);

        return TimingsElement.report(timings.toArray(TimingsElement[]::new));
    }

    public void gatherWorld() {
        gatherWorld.mark();
    }

    public void optimizeWorld() {
        optimizeWorld.mark();
    }

    public void createStepMath() {
        stepMath.add(new TimingsElement("stepMath"));
    }

    public void stepMath() {
        stepMath.get(stepMath.size() - 1).mark();
    }


    public void report() {
        ModuleSpawning.get().logger().info(this.toString());
    }
}
