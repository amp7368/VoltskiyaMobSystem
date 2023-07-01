package voltskiya.mob.system.storage.mob.clone;

import apple.mc.utilities.data.serialize.EntitySerializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import voltskiya.mob.system.storage.BaseEntity;
import voltskiya.mob.system.storage.mob.StoredLocation;

@Entity
@Table(name = "mob_clone")
public class DMobClone extends BaseEntity {

    @Id
    private UUID id;
    @Column(nullable = false, columnDefinition = "text")
    private String nbt;
    @Column(nullable = false)
    private String entityTypeKey;

    @Column(nullable = false)
    @Embedded(prefix = "location_")
    private StoredLocation location;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DMobTag> tags;

    public DMobClone(EntitySerializable entity, StoredLocation location) {
        this.location = location;
        this.tags = new ArrayList<>();
        this.nbt = entity.getEntityTag().toString();
        EntityType entityType = entity.getEntityType();
        this.entityTypeKey = Objects.requireNonNullElse(entityType, EntityType.ARMOR_STAND).getKey().asString();
    }

    public Location getLocation() {
        return location.toLocation();
    }

    public void spawn() {
        new EntitySerializable(nbt, NamespacedKey.fromString(entityTypeKey)).spawn(getLocation());
    }

    public void addTag(DMobTag tag) {
        this.tags.add(tag);
    }

    public List<DMobTag> getTags() {
        return this.tags;
    }
}
