import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WartungarbeitenCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
    {
        if(!sender.hasPermission("unitedworld.wartungsarbeiten"))
        {
            sender.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }
        
        Main.wartungsarbeiten = !Main.wartungsarbeiten;

        sender.sendMessage("§8Wartungsarbeiten: §l" + Main.wartungsarbeiten);

        if(!Main.wartungsarbeiten) return false;
        
        for(Player i : Bukkit.getOnlinePlayers())
        {
            i.kickPlayer("§8§lWartungarbeiten");
        }

        return false;
    }
}
