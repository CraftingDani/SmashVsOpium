import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatMessageListener implements Listener
{
    String message;

    @EventHandler
    private void onChatMessage(AsyncPlayerChatEvent e)
    {
        e.setCancelled(true);

        message = e.getMessage();
        replace(); // replace color codes to colored text

        Bukkit.broadcastMessage("§a" + e.getPlayer().getName() + "§2: §f" +  message);
    }

    private void replace()
    {
        Pattern pattern = Pattern.compile("&[a-f]");

        if(pattern.matcher(message).find())
        {
            message = message.replaceFirst("&", "§");
            replace();
        }

        pattern = Pattern.compile("&[0-8]");
        
        if(pattern.matcher(message).find())
        {
            message = message.replaceFirst("&", "§");
            replace();
        }
    }
}
