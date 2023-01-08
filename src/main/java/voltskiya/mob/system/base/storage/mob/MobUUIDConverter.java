package voltskiya.mob.system.base.storage.mob;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import voltskiya.mob.system.base.mob.MobUUID;

@Converter
public class MobUUIDConverter implements AttributeConverter<MobUUID, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MobUUID attribute) {
        return attribute.getId();
    }

    @Override
    public MobUUID convertToEntityAttribute(Integer dbData) {
        return new MobUUID(dbData);
    }
}
