import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener
{
    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e)
    {
        FileConfiguration config = Main.getPlugin().getConfig();
        Player killer;

        try 
        {
            killer = e.getEntity().getKiller();
            config.set("balances." + killer.getName(), config.getInt("balances." + killer.getName()) - 2);
            Main.updateScoreboard(killer);
            Main.getPlugin().checkNegativeMoney(killer);
        }
        catch(Exception ex)
        { 
            Player player = e.getEntity().getPlayer();
            config.set("balances." + player.getName(), config.getInt("balances." + player.getName()) - 2);
            Main.updateScoreboard(player);
            Main.getPlugin().checkNegativeMoney(player);
        }
        
        Main.getPlugin().saveConfig();
    }
}
