import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlaverMoveListener implements Listener
{
    @EventHandler
    private void onPlayerMovement(PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        int x = player.getLocation().getBlock().getX();
        int y = player.getLocation().getBlock().getY();
        int z = player.getLocation().getBlock().getZ();
        
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aX: §2" + x + " §aY: §2" + y + " §aZ: §2" + z));
    }
}
