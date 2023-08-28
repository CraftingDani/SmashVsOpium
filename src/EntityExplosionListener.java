import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplosionListener implements Listener
{
    @EventHandler
    private void onExplosion(EntityExplodeEvent e)
    {
        FileConfiguration config = Main.getPlugin().getConfig();
        
        
        for (Block i : e.blockList())
        {
            if(config.getString("protectedchests." + i) != null) e.blockList().remove(i);
            if(config.getString("expResBlocks." + i) != null) e.blockList().remove(i);
        }
        
        if(e.getLocation().getWorld() != Bukkit.getWorld("world")) return;

        Location start = new Location(Bukkit.getWorld("world"), 940, 100, 861);
        Location end = new Location(Bukkit.getWorld("world"), 970, 100, 891);
        Location block = e.getLocation();

        if(block.getX() <= end.getX() && block.getX() >= start.getX()
        && block.getZ() <= end.getZ() && block.getZ() >= start.getZ())
        {
            e.setCancelled(true);
        } 
    }
}
