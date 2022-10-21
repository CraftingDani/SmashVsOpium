import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceEventListener implements Listener
{
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e)
    {
        if(e.getBlock().getWorld() == Bukkit.getWorld("Event")) e.setCancelled(true);
    }
}
