import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener
{
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e)
    {
        Block above = Bukkit.getWorld("world").getBlockAt(e.getBlock().getLocation().add(0, 1, 0));
        FileConfiguration config = Main.getPlugin().getConfig();
        if(config.getString("protectedchests." + above) != null)
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cYou are not allowed to place a hopper underneath a protected barrel.");
            return;
        }

        if(e.getPlayer().isOp()) return;

        if(e.getBlock().getWorld() == Bukkit.getWorld("challenge")) e.setCancelled(true);
        if(e.getBlock().getWorld() == Bukkit.getWorld("prison")) e.setCancelled(true);

        if(e.getBlock().getWorld() != Bukkit.getWorld("world")) return;

        Location block = e.getBlock().getLocation();
        Location start = new Location(Bukkit.getWorld("world"), 940, 100, 861);
        Location end = new Location(Bukkit.getWorld("world"), 970, 100, 891);

        if(block.getX() <= end.getX() && block.getX() >= start.getX()
        && block.getZ() <= end.getZ() && block.getZ() >= start.getZ())
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cYou are not allowed to modify the spawn.");
        }
    }
}
