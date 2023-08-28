import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

        Player player = e.getPlayer();
        
        if(Main.checkPrison(player.getName()))
        {
            player.sendMessage("§cYou cannot write anything in the chat while you are in prison!");
            return;
        }

        message = e.getMessage();
        replace(); // replace color codes to colored text
        Bukkit.broadcastMessage(player.getDisplayName() + "§8: §f" +  message);
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

        pattern = Pattern.compile("&[l,o,k,m,r,n]");
        
        if(pattern.matcher(message).find())
        {
            message = message.replaceFirst("&", "§");
            replace();
        }
    }
}
