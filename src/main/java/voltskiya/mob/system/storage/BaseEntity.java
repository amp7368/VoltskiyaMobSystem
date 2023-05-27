package voltskiya.mob.system.storage;

import io.ebean.Model;
import io.ebean.annotation.DbName;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@DbName(MobDatabase.NAME)
public class BaseEntity extends Model {

    public BaseEntity() {
        super(MobDatabase.NAME);
    }
}
