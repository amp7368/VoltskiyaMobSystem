package voltskiya.mob.system.base;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDatabaseMap<ID, MC, T> {

    private final Map<ID, T> mapping = new HashMap<>();

    @Nullable
    public T get(@Nullable ID uuid) {
        if (uuid == null) return null;
        synchronized (this.mapping) {
            return this.mapping.get(uuid);
        }
    }

    @Nullable
    public T getFromMC(@Nullable MC minecraft) {
        if (minecraft == null) return null;
        return this.get(this.translateMinecraft(minecraft));
    }

    public abstract ID translateMinecraft(MC minecraft);

    public void put(@NotNull ID id, @NotNull T val) {
        this.mapping.put(id, val);
    }
}
