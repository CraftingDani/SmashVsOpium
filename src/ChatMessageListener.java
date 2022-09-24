import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatMessageListener implements Listener
{
    @EventHandler
    private void onChatMessage(AsyncPlayerChatEvent e)
    {
        e.setCancelled(true);
        Bukkit.broadcastMessage("§a" + e.getPlayer().getDisplayName() + "§2: §f" + e.getMessage());
    }
}
