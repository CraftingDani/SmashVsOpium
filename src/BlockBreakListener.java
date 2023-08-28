import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class BlockBreakListener implements Listener
{
    @EventHandler
    private void onBlockBreak(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        FileConfiguration config = Main.getPlugin().getConfig();
        Player player = e.getPlayer();
        
        
        if(config.getString("protectedchests." + block) != null)
        {
            player.sendMessage("§cYou can not break a protected barrel.");
            e.setCancelled(true);
        }


        // spawner
        
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if(e.getBlock().getType() == Material.SPAWNER  &&  itemInHand.getType() == Material.NETHERITE_PICKAXE  &&  itemInHand.getItemMeta().getDisplayName().equals("§8§lSpawner Breaker"))
        {
            //   <= 0   -> break
            int remainungUses = Integer.parseInt(itemInHand.getItemMeta().getLore().get(1));
            remainungUses--;
            
            ItemMeta pickMeta = itemInHand.getItemMeta();
            List<String> lore = pickMeta.getLore();
            lore.set(1, "" + (remainungUses) + "");

            pickMeta.setLore(lore);
            itemInHand.setItemMeta(pickMeta);
            
            if(player.getInventory().firstEmpty() == -1) // inv full
            {
                e.setCancelled(true);
                player.sendMessage("§cYour inventory is full.");
                return;
            }
            
            //give spawner
            
            player.getInventory().addItem(new ItemStack(Material.SPAWNER));
            
            if(remainungUses <= 0) //used up
            {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
            return;
        }

        luckyBlock(e);

        List<Location> plates;
        Location above = block.getRelative(0, 1, 0).getLocation();
        plates = (List<Location>) config.getList("invPlates");
        if(plates == null) { plates = new ArrayList<Location>(); }
        
        for(Location i : plates)
        {
            if(above.getX() == i.getX() && above.getY() == i.getY() && above.getZ() == i.getZ())
            {
                e.setCancelled(true);
                return;
            }
        }


        if(player.isOp()) return;
        if(block.getWorld() == Bukkit.getWorld("challenge")) e.setCancelled(true);
        if(block.getWorld() == Bukkit.getWorld("prison")) e.setCancelled(true);
        if(e.getBlock().getWorld() != Bukkit.getWorld("world")) return;
        if(e.getBlock().getLocation() == Main.TEAM_GREEN_LOC || e.getBlock().getLocation() == Main.TEAM_RED_LOC) return;

        // spawn prot.
        Location start = new Location(Bukkit.getWorld("world"), 940, 100, 861);
        Location end = new Location(Bukkit.getWorld("world"), 970, 100, 891);

        if(block.getX() <= end.getX() && block.getX() >= start.getX()
        && block.getZ() <= end.getZ() && block.getZ() >= start.getZ())
        {
            e.setCancelled(true);
            player.sendMessage("§cYou are not allowed to modify the spawn.");
        } 
    }

    private static void luckyBlock(BlockBreakEvent e)
    {
        e.getPlayer().sendMessage("luckyblock1");
        if(e.getBlock().getType() != Material.PLAYER_HEAD) return;
        e.getPlayer().sendMessage("luckyblock2");

        Skull skull = (Skull) e.getBlock().getState();
        if(skull.getOwningPlayer() != Bukkit.getOfflinePlayer(UUID.fromString("bbd89297-0f8b-4d8d-a045-2b0f07239c66"))) return; //luckyblock skull owner
        e.getPlayer().sendMessage("luckyblock final");
    }
}
