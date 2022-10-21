import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerJoinListener implements Listener
{
    FileConfiguration config = Main.getPlugin().getConfig();
    
    @EventHandler
    private void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        e.setJoinMessage("§a→ " + player.getName());
        Main.updateScoreboard(player);
        player.setDisplayName("§a" + player.getName());
        player.setPlayerListName("§a" + player.getName());
        player.teleport(config.getLocation("lastLocation." + player.getName()));
        player.setGameMode(GameMode.SURVIVAL);
        player.sendTitle("", "§f§k- §8Welcome to §a§lUnited§2§lWorld§8 §f§k-", 5, 75, 5);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f§k- §a§lUnited§2§lWorld §f§k-"));
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        e.setQuitMessage("§c← " + player.getName());
        if(config.get("timeScheduler." + player.getName()) != null)
        {
            Bukkit.getScheduler().cancelTask(config.getInt("timeScheduler." + player.getName()));
            Bukkit.getScheduler().cancelTask(config.getInt("spawnScheduler." + player.getName()));
            config.set("timeScheduler." + player.getName(), null);
            config.set("spawnScheduler." + player.getName(), null);
        }
        else
        {
            config.set("lastLocation." + player.getName(), player.getLocation());
        }

        Main.getPlugin().saveConfig();
    }
}
