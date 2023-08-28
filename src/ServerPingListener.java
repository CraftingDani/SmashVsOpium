import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener implements Listener
{
    @EventHandler
    private void onServerPing(ServerListPingEvent e)
    {
        e.setMotd("                    \u00A78\u00A7k- \u00A7a\u00A7lSmash\u00A78Vs\u00A7c§lOpium \u00A78\u00A7k-");
        if(Main.wartungsarbeiten) e.setMotd("                    \u00A78\u00A7k- \u00A7a\u00A7lSmash\u00A78Vs\u00A7c§lOpium \u00A78\u00A7k-\n                   \u00A78\u00A7lWartungsarbeiten");
    }
}
