package voltskiya.mob.system.base.biome;

import java.util.UUID;

public class BiomeUUID {

    public UUID uuid = UUID.randomUUID();

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BiomeUUID other && other.uuid.equals(this.uuid);
    }
}
