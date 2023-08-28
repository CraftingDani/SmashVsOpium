import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }

        Player player = (Player) sender;

        if(args.length > 0)
        {
            if(!player.hasPermission("svo.ec.others"))
            {
                player.sendMessage("§cYou can only open your own enderchest. Please use §4/" + label + ".");
                return false;
            }

            try { player.openInventory(Bukkit.getPlayer(args[0]).getEnderChest()); }
            catch(Exception e) { player.sendMessage("§cError"); }
            return false;
        }

        if(player.hasPermission("svo.ec"))
        {
            player.openInventory(player.getEnderChest());
            return false;
        }
        
        player.sendMessage("§cYou do not have the permission to use this command.");
        return false;
    }
}
