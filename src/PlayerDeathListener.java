import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener
{
    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e)
    {
        if(!(e.getEntity() instanceof Player)) return;

        FileConfiguration config = Main.getPlugin().getConfig();
        Player player = e.getEntity().getPlayer();
        Player killer = player.getKiller();


        if((config.get("timeScheduler." + player.getName()) != null) && (e.getEntity().getWorld() == Bukkit.getWorld("challenge"))) // if the player was in the challenge world
        {
            Bukkit.getScheduler().cancelTask(config.getInt("timeScheduler." + player.getName()));
            Bukkit.getScheduler().cancelTask(config.getInt("spawnScheduler." + player.getName()));
            Bukkit.getScheduler().cancelTask(config.getInt("challengeTimeScheduler." + player.getName()));
            config.set("timeScheduler." + player.getName(), null);
            config.set("spawnScheduler." + player.getName(), null);
            config.set("challengeTimeScheduler." + player.getName(), null);
            
            Main.getPlugin().saveConfig();
        }
        
        
        //-- player was killer by another player --\\
        
        if(!(killer instanceof Player)) return;

        
        if(Main.getPlayerTeamName(player.getName()).equals(Main.getPlayerTeamName(killer.getName()))) // same team
        {
            // (/forgive)
            config.set("hasKilled." + killer.getName(), player.getName());
            killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6);
        }
        else                                                                                         // different team
        {
            for(ItemStack i : player.getInventory())
            {
                if(i == null) continue;
                player.sendMessage("bye bye " + i.toString());
                player.getWorld().dropItemNaturally(player.getLocation(), i);
            }

            player.getInventory().clear();
        }
    }
}
