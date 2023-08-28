import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener
{
    @EventHandler
    public void onPlayerAchievement(PlayerAdvancementDoneEvent e)
    {
        /* 
        if(!e.getAdvancement().getKey().toString().contains("blazeandcave:")) return;

        if(e.getAdvancement().getKey().toString().equals("blazeandcave:technical/lucky_break_check")) return; // prevent bug when falling

        FileConfiguration config = Main.getPlugin().getConfig();
        Player player = e.getPlayer();
        
        config.set("balances." + player.getName(), config.getInt("balances." + player.getName()) + Main.ACHIEVEMENT_MONEY);
        Main.getPlugin().saveConfig();
        Main.updateScoreboard(player);

        player.sendMessage("§8You earned " + Main.ACHIEVEMENT_MONEY + " Coins for completing a custom achievement!");

        */
    }
}
