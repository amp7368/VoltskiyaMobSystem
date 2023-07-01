package voltskiya.mob.system.base.selector.api;

import com.google.gson.JsonElement;
import voltskiya.apple.utilities.database.base.JsonApiDataQuery;
import voltskiya.apple.utilities.database.base.JsonDataName;
import voltskiya.mob.system.VoltskiyaMobPlugin;
import voltskiya.mob.system.base.selector.SpawnSelector;
import voltskiya.mob.system.base.selector.SpawnSelectorDatabase;

public class SpawnSelectorDatabaseQuery extends JsonApiDataQuery<SpawnSelector> {

    public SpawnSelectorDatabaseQuery(SpawnSelector selector) {
        super(JsonDataName.create(VoltskiyaMobPlugin.get().getName(), "SpawnSelector", selector.getSaveFileName()), selector);
    }


    @Override
    public SpawnSelector update(JsonElement inst) {
        SpawnSelector selector = SpawnSelectorDatabase.gson().fromJson(inst, SpawnSelector.class);
        SpawnSelectorDatabase.save(selector);
        return selector;
    }
}
