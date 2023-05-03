package voltskiya.mob.system.base.spawner;

import apple.utilities.json.gson.GsonBuilderDynamic;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import voltskiya.mob.system.base.spawner.attribute.GsonMapSpawningAttribute;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningAttributeModifier;
import voltskiya.mob.system.base.spawner.attribute.base.SpawningAttributes;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.modifier.GsonMapSpawningModifier;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifierFactory;
import voltskiya.mob.system.base.spawner.rule.generic.GsonMapSpawningRule;
import voltskiya.mob.system.base.spawner.rule.generic.SpawningRule;
import voltskiya.mob.system.base.spawner.rule.temporal.GsonMapSpawningTemporalRule;
import voltskiya.mob.system.base.spawner.rule.temporal.SpawningTemporalRule;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class Spawner {

    protected List<SpawningRule> rules = new ArrayList<>();
    protected List<SpawningTemporalRule> temporalRules = new ArrayList<>();
    protected List<SpawningModifierFactory> modifiers = new ArrayList<>();

    protected List<SpawningAttributeModifier> attributes = new ArrayList<>();

    public Spawner() {
    }

    public static GsonBuilderDynamic gson(GsonBuilderDynamic gson) {
        GsonMapSpawningRule.gson(gson);
        GsonMapSpawningModifier.gson(gson);
        GsonMapSpawningAttribute.gson(gson);
        GsonMapSpawningTemporalRule.gson(gson);
        return gson;
    }

    public static Spawner biomeDefault() {
        return new Spawner();
    }

    public void addRule(SpawningRule rule) {
        rules.add(rule);
    }

    public void addTemporalRule(SpawningTemporalRule rule) {
        temporalRules.add(rule);
    }

    public void addModifier(SpawningModifierFactory modifier) {
        modifiers.add(modifier);
    }

    public void addAttribute(SpawningAttributeModifier attribute) {
        attributes.add(attribute);
    }

    public boolean isBreaksRule(SpawningContext context) {
        for (SpawningRule rule : rules) {
            if (rule.isBreaksRule(context)) return true;
        }
        return false;
    }

    public long spawnDelay(SpawningContext context) {
        long spawnDelay = 0;
        if (temporalRules.isEmpty()) return spawnDelay;
        for (SpawningTemporalRule rule : temporalRules) {
            spawnDelay = Math.max(spawnDelay, rule.spawnDelay(context));
        }
        return Bukkit.getCurrentTick() + spawnDelay;
    }

    public void prepareModifiers(SpawningContext context, SpawnerSummonResult result) {
        for (SpawningModifierFactory modifier : this.modifiers) {
            modifier.createModifier(context, result);
        }
    }

    public void attributes(SpawningAttributes original) {
        original.join(SpawningAttributes.of(this.attributes));
    }

}
