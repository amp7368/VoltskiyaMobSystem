package voltskiya.mob.system.base.storage.world;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity()
public class StoredWorld {

    @Id
    @Column(nullable = false)
    public short id;

    @Column(nullable = false)
    public UUID bukkit;

    @Column(nullable = false)
    public long spawnSuccessCount = 0;

    @Column(nullable = false)
    public long spawnFailCount = 0;

    public StoredWorld(short id, UUID bukkit) {
        this.id = id;
        this.bukkit = bukkit;
    }

    public StoredWorld() {
    }
}
