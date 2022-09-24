import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener implements Listener
{
    @EventHandler
    private void onServerPing(ServerListPingEvent e)
    {
        e.setMotd("                   \u00A7f\u00A7k- \u00A7r\u00A7a\u00A7lUnited\u00A72\u00A7lWorld \u00A7f\u00A7k-");
    }
}
