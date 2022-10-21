import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener
{
    @EventHandler
    private void onEntityDeath(EntityDeathEvent e)
    {
        if(e.getEntity().getWorld() == Bukkit.getWorld("Event")) 
        {
            e.getDrops().clear();
            if(e.getEntity().getKiller() != null) e.setDroppedExp(32);
        }
    }
}
