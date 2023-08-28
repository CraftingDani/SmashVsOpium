import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ExtraHeartCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("svo.extraheart"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < 20)
        {
            player.sendMessage("§cYou do not have the full amount of regular hearts.");
            return false;
        }

        if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > 40)
        {
            player.sendMessage("§cYou already have the maximum amount of extra hearts.");
            return false;
        }

        FileConfiguration config = Main.getPlugin().getConfig();
        
        if(config.getInt("balances." + player.getName()) < Main.EXTRAHEART_MONEY)
        {
            player.sendMessage("§cYou do not have enough money.");
            return false;
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() +2);
        player.sendMessage("§aYou bought an extra heart for §2" + Main.HEART_MONEY + "§a coins.");
        config.set("balances." + player.getName(), config.getInt("balances." + player.getName()) - Main.EXTRAHEART_MONEY);
        Main.updateScoreboard(player);
        Main.getPlugin().saveConfig();

        return false;
    }
}
