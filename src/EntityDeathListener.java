import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDeathListener implements Listener
{
    @EventHandler
    private void onEntityDeath(EntityDeathEvent e)
    {
        if(e.getEntity().getWorld() == Bukkit.getWorld("challenge") && !(e.getEntity() instanceof Player)) 
        {
            e.getDrops().clear();
            if(e.getEntity().getKiller() != null) e.setDroppedExp(32);
        }

        if(!(e.getEntity().getKiller() instanceof Player)) return;

        Entity entity = e.getEntity();
        Random rand = new Random();
        double chance = 0.01; 
        EntityType entityType = entity.getType();
        List<EntityType> allowedMobs = Arrays.asList(EntityType.SKELETON, EntityType.PIG, EntityType.ZOMBIE, EntityType.SPIDER, EntityType.CHICKEN, EntityType.STRIDER);
        
        if (allowedMobs.contains(entityType) && rand.nextDouble() < chance)
        {
            ItemStack spawnEgg = new ItemStack(Material.valueOf(entityType.name() + "_SPAWN_EGG"));
            e.getDrops().add(spawnEgg);
        }
    }
}
