import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener
{
    @EventHandler
    private void onRespawn(PlayerRespawnEvent e)
    {
        FileConfiguration config = Main.getPlugin().getConfig();
        Player player = e.getPlayer();
        
        config.set("spawnProtection." + player.getName(), true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                config.set("spawnProtection." + player.getName(), false);
            }
        }, 10 * 20);
    }
}
