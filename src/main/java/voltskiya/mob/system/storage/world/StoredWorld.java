package voltskiya.mob.system.storage.world;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import voltskiya.mob.system.storage.BaseEntity;

@Entity()
@Table(name = "stored_world")
public class StoredWorld extends BaseEntity {

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
