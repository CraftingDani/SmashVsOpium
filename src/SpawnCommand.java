import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3)
    {
        Player player = (Player) sender;

        if(!sender.hasPermission("svo.spawn"))
        {
            sender.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        if(Main.checkPrison(player.getName()))
        {
            player.sendMessage("§cYou cannot use this command while you are in prison!");
            return false;
        }
        
        FileConfiguration config = Main.getPlugin().getConfig();
        if(!config.getBoolean("spawnPerm." + Main.getPlayerOpponentTeamName(player.getName())))
        {
            player.sendMessage("§cNobody is in your base!");
            return false;
        }

        player.teleport(new Location(Bukkit.getWorld("world"), 955, 91, 876));

        return false;
    }     
}
