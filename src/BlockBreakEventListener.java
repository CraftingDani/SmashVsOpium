import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventListener implements Listener
{
    @EventHandler
    private void onBlockBreak(BlockBreakEvent e)
    {
        if(e.getBlock().getWorld() == Bukkit.getWorld("Event")) e.setCancelled(true);
    }
}
