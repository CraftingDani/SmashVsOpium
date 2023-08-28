//import org.bukkit.Bukkit;
//import org.bukkit.block.Barrel;
//import org.bukkit.block.Hopper;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.craftbukkit.v1_17_R1.block.CraftBarrel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class HopperListener implements Listener
{
    @EventHandler
    private void onHop(InventoryMoveItemEvent e)
    {
        /*Bukkit.broadcastMessage(e.getSource().getHolder());
        
        if(!(e.getSource().getHolder() instanceof CraftBarrel) || !(e.getDestination().getHolder() instanceof Hopper)) return;
        
        FileConfiguration config = Main.getPlugin().getConfig();
        Barrel chest = (Barrel) e.getSource().getHolder();

        if(config.getString("protectedchests." + chest.getBlock()) != null)
        
        e.setCancelled(true);
        */
    }
}
