package voltskiya.mob.system.base.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;

public class MaterialDatabase {

    public Set<Material> materials = new HashSet<>();

    public MaterialDatabase() {
        materials.addAll(List.of(Material.values()));
    }
}
