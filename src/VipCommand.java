import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class VipCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }   

        Player player = (Player) sender;

        if(!player.hasPermission("svo.vip")) return false;

        FileConfiguration config = Main.getPlugin().getConfig();
        
        if(player.getWorld() == Bukkit.getWorld("vip"))
        {
            player.teleport(config.getLocation("lastLocation." + player.getName()));
            return false;
        }

        config.set("lastLocation." + player.getName(), player.getLocation());
        player.teleport(new Location(Bukkit.getWorld("vip"), -1641.5, 80, -2079.5));
        Main.getPlugin().saveConfig();

        return false;
    }
}
