import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener
{
    @EventHandler
    private void onClick(InventoryClickEvent e)
    {
        if(!e.getView().getTitle().equals("§a§lAdmin§c§lShop")) return;
        if(e.getCurrentItem() == null) return;
        
        ItemStack item = new ItemStack(e.getCurrentItem());
        Player player = (Player) e.getWhoClicked();
        FileConfiguration config = Main.getPlugin().getConfig();
        int balance = config.getInt("balances." + player.getName());
        int price = e.getCurrentItem().getAmount();
        
        e.setCancelled(true);
        
        if(e.getClickedInventory().getHolder() instanceof Player) return;

        item.setAmount(1);
        
        if(item.getItemMeta().getDisplayName().equals(" ")) return;

        if(balance < price)
        {
            player.sendMessage("§cYou do not have enough money. §8Price: " + price);
            player.closeInventory();
            return;
        }

        if(player.getInventory().firstEmpty() == -1)
        {
            player.sendMessage("§cYour inventory is full.");
            player.closeInventory();
            return;
        }

        if(item.getType() == Material.LIGHT) item.setAmount(4);

        config.set("balances." + player.getName(), balance - price);
        player.sendMessage("§8You bought '" + item.getItemMeta().getDisplayName() + "§8' for " + price + " coins.");
        player.getInventory().addItem(item);
        Main.updateScoreboard(player);
        Main.getPlugin().saveConfig();
    }
}
