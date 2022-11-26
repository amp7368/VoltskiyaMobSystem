package voltskiya.mob.system.base.storage.world;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class WorldUUID {

    public short worldId;
    public UUID uuid;

    public WorldUUID(short worldId, UUID uuid) {
        this.worldId = worldId;
        this.uuid = uuid;
    }

    public WorldUUID() {
    }

    @Nullable
    public World getBukkit() {
        return Bukkit.getWorld(uuid);
    }
}
