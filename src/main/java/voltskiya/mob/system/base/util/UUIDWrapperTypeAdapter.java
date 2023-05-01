package voltskiya.mob.system.base.util;

import apple.utilities.json.gson.serialize.JsonSerializing;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.function.Function;

public class UUIDWrapperTypeAdapter<Id, T extends UUIDWrapper<?, ?>> implements JsonSerializing<T> {

    private final Function<Id, T> create;
    private final Class<Id> idType;

    public UUIDWrapperTypeAdapter(Function<Id, T> create, Class<Id> idType) {
        this.create = create;
        this.idType = idType;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getId(), idType);
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return create.apply(context.deserialize(json, idType));
    }
}
