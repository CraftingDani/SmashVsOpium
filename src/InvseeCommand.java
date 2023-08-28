import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args)
    {
        
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("unitedworld.invsee"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        player.openInventory(Bukkit.getPlayer(args[0]).getInventory());

        return false;
    }
}
