package voltskiya.mob.system.storage.mob.clone;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import voltskiya.mob.system.storage.BaseEntity;

@Entity
@Table(name = "mob_tag")
public class DMobTag extends BaseEntity {

    @Id
    private UUID id;
    @ManyToOne(optional = false)
    private DMobClone mob;
    @Column(nullable = false)
    private String tag;

    public DMobTag(String tag) {
        this.tag = tag;
    }
}
