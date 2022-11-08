package voltskiya.mob.system.spawning.storage;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import voltskiya.mob.system.base.mob.MobUUID;

@Converter
public class MobUUIDConverter implements AttributeConverter<MobUUID, Short> {

    @Override
    public Short convertToDatabaseColumn(MobUUID attribute) {
        return attribute.typeUUID;
    }

    @Override
    public MobUUID convertToEntityAttribute(Short dbData) {
        return new MobUUID(dbData);
    }
}
