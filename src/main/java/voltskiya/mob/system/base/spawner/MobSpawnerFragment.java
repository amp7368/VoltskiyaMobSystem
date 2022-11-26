package voltskiya.mob.system.base.spawner;

import apple.utilities.json.gson.GsonBuilderDynamic;
import java.util.ArrayList;
import java.util.List;
import voltskiya.mob.system.base.mob.MobUUID;
import voltskiya.mob.system.base.spawner.context.SpawningContext;
import voltskiya.mob.system.base.spawner.modifier.PreSpawningModifierFactory;
import voltskiya.mob.system.base.spawner.modifier.SpawningModifierFactory;
import voltskiya.mob.system.base.spawner.rule.generic.SpawningRule;
import voltskiya.mob.system.base.spawner.rule.old.BlockConditionsTypes;
import voltskiya.mob.system.base.spawner.rule.temporal.SpawningTemporalRule;
import voltskiya.mob.system.player.world.mob.SpawnerSummonResult;

public class MobSpawnerFragment {

    private MobUUID mob;
    private final List<SpawningRule> rules = new ArrayList<>();
    private final List<SpawningTemporalRule> temporalRules = new ArrayList<>();
    private final List<SpawningModifierFactory> modifiers = new ArrayList<>();
    private final List<PreSpawningModifierFactory> preModifiers = new ArrayList<>();

    public MobSpawnerFragment(MobUUID mob) {
        this.mob = mob;
    }

    public MobSpawnerFragment() {
    }

    public static void gson(GsonBuilderDynamic gson) {
        BlockConditionsTypes.register(gson);
    }

    public void addRule(SpawningRule rule) {
        rules.add(rule);
    }

    public void addTemporalRule(SpawningTemporalRule rule) {
        temporalRules.add(rule);
    }

    public void addPreModifier(PreSpawningModifierFactory rule) {
        preModifiers.add(rule);
    }

    public void addModifier(SpawningModifierFactory modifier) {
        modifiers.add(modifier);
    }

    public boolean isBreaksRule(SpawningContext context) {
        for (SpawningRule rule : rules) {
            if (rule.isBreaksRule(context))
                return true;
        }
        return false;
    }

    public long spawnDelay(SpawningContext context) {
        long spawnDelay = 0;
        for (SpawningTemporalRule rule : temporalRules) {
            spawnDelay = Math.max(spawnDelay, rule.spawnDelay(context));
        }
        return spawnDelay;
    }

    public void preModify(SpawningContext context, SpawnerSummonResult result) {
        for (PreSpawningModifierFactory modifier : this.preModifiers) {
            modifier.createModifier(context).preModify(result);
        }
    }

    public void modify(SpawningContext context, SpawnerSummonResult result) {
        for (SpawningModifierFactory modifier : this.modifiers) {
            result.addModifier(modifier.createModifier(context));
        }
    }

    public MobUUID getUUID() {
        return this.mob;
    }
}
