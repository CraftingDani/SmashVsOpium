import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlaverInteractListener implements Listener
{
    @EventHandler
    private void onRightclick(PlayerInteractEvent e)
    {
        FileConfiguration config = Main.getPlugin().getConfig();
        Block block = e.getClickedBlock();
        Player player = e.getPlayer();
        

        if(config.getString("protectedchests." + block) != null && !config.getString("protectedchests." + block).equals(player.getName()))
        {
            player.sendMessage("§cThis barrel is protected by " + config.getString("protectedchests." + block));
            e.setCancelled(true);
        }

        if(player.getInventory().getItemInMainHand().getType() == Material.STICK && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("§8§lGhost "))
        {
            String material = null;

            if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Dirt"))
            {
                material = "dirt";
                placeGhostBlock(e, material);
                return;
            }
            if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Grass Block"))
            {
                material = "grass_block";
                placeGhostBlock(e, material);
                return;
            }
            if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Cobblestone"))
            {
                material = "cobblestone";
                placeGhostBlock(e, material);
                return;
            }
            if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Stone"))
            {
                material = "stone";
                placeGhostBlock(e, material);
                return;
            }

            if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Block Remover"))
            {
                removeGhostBlock(e);
                return;
            }
        }


        if(player.getInventory().getItemInMainHand().getType() == Material.STICK && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("§8§lInvisible Pressure Plate Remover"))
        {
            removeInvPlate(e);
            return;
        }

        
        
        if(player.getInventory().getItemInMainHand().getType() == Material.STICK && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("§8§lInvisible Pressure Plate"))
        {
            placeInvPlate(e);
            return;
        }


        if(player.getInventory().getItemInMainHand().getType() == Material.STICK && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("§8§lExplosion Protector"))
        {
            expProtectBlocks(e);
            return;
        }
    }



    private void removeInvPlate(PlayerInteractEvent e)
    {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock().getWorld() != Bukkit.getWorld("world")) return;
        FileConfiguration config = Main.getPlugin().getConfig();
        Location above = e.getClickedBlock().getRelative(0, 1, 0).getLocation();
        
        List<Location> plates;
        plates = (List<Location>) config.getList("invPlates");
        if(plates == null) { plates = new ArrayList<Location>(); }
        

        for(Location i : plates)
        {
            if(!i.equals(above)) continue;  // check if there is a plate
        }


        plates.remove(above);

        config.set("activatedBlockMaterials." + above.getBlock().getLocation(), null);

        config.set("invPlates", plates);
        config.set("invPlatePlayers." + above, null);
        Main.getPlugin().saveConfig();
    }


    private void removeGhostBlock(PlayerInteractEvent e)
    {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        FileConfiguration config = Main.getPlugin().getConfig();
        Location location = e.getClickedBlock().getLocation().add(0, 1, 0);
        
        config.set("wtBlockMaterials." + location, null);
        config.set("wtBlockPlayers." + location, null);

        List<Location> blocks;
        blocks = (List<Location>) config.getList("wtBlocks");
        if(blocks == null)
        {
            blocks = new ArrayList<Location>();
        }

        blocks.remove(location);
        config.set("wtBlocks", blocks);
        Main.getPlugin().saveConfig();
        Main.getPlugin().updateGhostBlocks();
    }


    private void placeGhostBlock(PlayerInteractEvent e, String material)
    { 
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock().getWorld() != Bukkit.getWorld("world")) return;
        FileConfiguration config = Main.getPlugin().getConfig();
        Location location = e.getClickedBlock().getLocation().add(0, 1, 0);
        if(config.getString("wtBlockMaterials." + location) != null) return; //already a wtBlock there

        e.setCancelled(true);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "summon falling_block " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() +  " {BlockState:{Name:\"minecraft:" + material +"\"},NoGravity:1b,Time:1,DropItem:0b}");
        e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
        
        List<Location> blocks;
        blocks = (List<Location>) config.getList("wtBlocks");
        if(blocks == null)
        {
            blocks = new ArrayList<Location>();
        }

        blocks.add(e.getClickedBlock().getLocation().add(0, 1, 0));

        config.set("wtBlocks", blocks);
        config.set("wtBlockMaterials." + e.getClickedBlock().getLocation().add(0, 1, 0), material);
        config.set("wtBlockPlayers." + e.getClickedBlock().getLocation().add(0, 1, 0), e.getPlayer().getName());
        Main.getPlugin().saveConfig();
    }


    private void placeInvPlate(PlayerInteractEvent e)
    {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock().getWorld() != Bukkit.getWorld("world")) return;
        FileConfiguration config = Main.getPlugin().getConfig();
        Location plate = e.getClickedBlock().getRelative(0, 1, 0).getLocation();
        
        List<Location> plates;
        plates = (List<Location>) config.getList("invPlates");
        if(plates == null) { plates = new ArrayList<Location>(); }
        

        for(Location i : plates)
        {
            if(i.equals(plate)) return; // already one there
        }


        e.setCancelled(true);
        e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);


        plates.add(plate);

        config.set("activatedBlockMaterials." + plate.getBlock().getLocation(), plate.getWorld().getBlockAt(plate).getRelative(0, -1, 0).getType().toString());

        config.set("invPlates", plates);
        config.set("invPlatePlayers." + plate, e.getPlayer().getName());
        Main.getPlugin().saveConfig();
    }

    
    private void expProtectBlocks(PlayerInteractEvent e)
    {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock().getWorld() != Bukkit.getWorld("world")) return;
        FileConfiguration config = Main.getPlugin().getConfig();
        Block block = e.getClickedBlock();
        Player player = e.getPlayer();
        int balance = config.getInt("balances." + player.getName());

        if(config.getString("expResBlocks." + block) != null) 
        {
            player.sendMessage("§8This block has already been protected from explosions.");
            return;
        }

        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        player.sendMessage("§8You protected this block from any explosions.");

        //config.set("balances." + player.getName(), balance - Main.EXP_PROT_MONEY);
        config.set("expResBlocks." + block, player.getName());
        Main.getPlugin().saveConfig();

        /*for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                for(int k = -1; k < 2; k++)
                {
                    if(config.getString("expResBlocks." + block.getRelative(k, j, i)) != null) continue;
                    config.set("expResBlocks." + block.getRelative(k, j, i), e.getPlayer().getName());
                }
            }
        }*/
    }
}
