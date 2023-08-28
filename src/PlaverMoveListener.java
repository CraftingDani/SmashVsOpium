import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlaverMoveListener implements Listener
{
    FileConfiguration config = Main.getPlugin().getConfig();

    @EventHandler
    private void onPlayerMovement(PlayerMoveEvent e)
    {
        Player player = e.getPlayer();

        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        
        String playerTeamColor = Main.getPlayerTeamColor(player.getName());

        if(player.getWorld() != Bukkit.getWorld("challenge")) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8X: " + playerTeamColor + x + " §8Y: " + playerTeamColor + y + " §8Z: " + playerTeamColor + z));

        pressurePlates(player, x, y, z);

        moneyBlock(player, x, y, z);
    }


    private void pressurePlates(Player player, int x, int y, int z)
    {
        List<Location> plates = (List<Location>) Main.getPlugin().getConfig().getList("invPlates");
        if(plates == null || plates.size() < 1) return;
        for(Location plate : plates)
        {
            if(x == plate.getBlockX() && y == plate.getBlockY() && z == plate.getBlockZ())
            {
                activateInvPlate(player, plate);
                continue;
            }

            if(config.getString("activatedBlockPlayers." + plate) == null) continue;
            if(!config.getString("activatedBlockPlayers." + plate).equals(player.getName())) return;
            
            Bukkit.getWorld("world").getBlockAt(plate).getRelative(0, -1, 0).setType(Material.getMaterial(config.getString("activatedBlockMaterials." + plate)));
            
            config.set("activatedBlock." + plate, false);
            config.set("activatedBlockPlayers." + plate, null);
            Main.getPlugin().saveConfig();
        }
    }


    private void moneyBlock(Player player, int x, int y, int z)
    {
        if((x == Main.TEAM_GREEN_LOC.getBlockX() && y == Main.TEAM_GREEN_LOC.getBlockY() && z == Main.TEAM_GREEN_LOC.getBlockZ() && Main.getPlayerTeamName(player.getName()).equals(Main.TEAM_RED_NAME))
        || (x == Main.TEAM_RED_LOC.getBlockX() && y == Main.TEAM_RED_LOC.getBlockY() && z == Main.TEAM_RED_LOC.getBlockZ() && Main.getPlayerTeamName(player.getName()).equals(Main.TEAM_GREEN_NAME)))
        {
            giveMoney(player);
            return;
        }
        
        if(config.get("enemyScheduler." + player.getName()) != null)
        {
            Bukkit.getScheduler().cancelTask(config.getInt("enemyScheduler." + player.getName()));
            config.set("enemyScheduler." + player.getName(), null);
            config.set("spawnPerm." + Main.getPlayerTeamName(player.getName()), false);
            Main.getPlugin().saveConfig();
        }
    }


    private void giveMoney(Player player)
    {
        FileConfiguration config = Main.getPlugin().getConfig();
        if(config.get("enemyScheduler." + player.getName()) != null) return;

        config.set("spawnPerm." + Main.getPlayerTeamName(player.getName()), true);
        Main.getPlugin().saveConfig();
        
        int enemyScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
        {
            int count = 20;

            @Override
            public void run()
            {
                ArrayList<Player> enemies = new ArrayList<Player>();
                for (Player i : Bukkit.getOnlinePlayers())
                {
                    if(Main.getPlayerTeamName(i.getName()).equals(Main.getPlayerTeamName(player.getName()))) continue; //same team as player
                    enemies.add(i);
                }

                if(enemies.size() == 0) return; // return if no enemy online
                
                for(Player i : enemies)
                {
                    i.sendTitle("", "§c§l" + player.getName() + " is in your base!", 5, 75, 5);
                }

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8" + count));

                count--;
                if(count > -1) return; // count <= 0
                config.set("balances." + player.getName(), config.getInt("balances." + player.getName()) +1);
                Main.getPlugin().saveConfig();
                Main.updateScoreboard(player);
                count = 20;
            }
        }, 0 * 20, 1 * 20);
        config.set("enemyScheduler." + player.getName(), enemyScheduler);
    }


    private void activateInvPlate(Player player, Location plate)
    {
        FileConfiguration config = Main.getPlugin().getConfig();
        if(config.getBoolean("activatedBlock." + plate)) return;

        Block plateBlock = plate.getWorld().getBlockAt(plate).getRelative(0, -1, 0);

        plateBlock.setType(Material.REDSTONE_BLOCK);

        config.set("activatedBlocks" + plate.getBlock().getLocation(), true);
        config.set("activatedBlockPlayers." + plate.getBlock().getLocation(), player.getName());
        Main.getPlugin().saveConfig();
    }
}
