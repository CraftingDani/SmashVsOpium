import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DropItemListener implements Listener
{
    @EventHandler
    private void onItemDrop(PlayerDropItemEvent e)
    {
        ItemStack item = e.getItemDrop().getItemStack();
        if(item.getType() == Material.NETHERITE_PICKAXE  &&  item.getItemMeta().getDisplayName().equals("§8§lSpawner Breaker")) e.setCancelled(true);
    }
}
