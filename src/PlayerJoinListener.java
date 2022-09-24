import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener
{
    @EventHandler
    private void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        e.setJoinMessage("§a→ " + player.getName());
        Main.updateScoreboard(player);
        player.setDisplayName("§a" + player.getName());
        player.setPlayerListName("§a" + player.getName());
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        e.setQuitMessage("§c← " + player.getName());
    }
}
