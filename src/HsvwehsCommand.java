import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HsvwehsCommand implements CommandExecutor
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
        
        if(!sender.hasPermission("svo.hsvwehs"))
        {
            sender.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        String paragraph;
        OfflinePlayer target;
        
        try
        {
            target = Bukkit.getOfflinePlayer(args[0]);
            paragraph = args[1];
        }
        catch(Exception e)
        {
            player.sendMessage("§cPlease use /" + label + " <player> <paragraph>.");
            return false;
        }

        if(args.length != 2)
        {
            player.sendMessage("§cPlease use /" + label + " <player> <paragraph>.");
            return false;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
        String timeStamp = dtf.format(LocalDateTime.now());

        Main.getPlugin().logToFile("hsvwehsLog.txt", "time: " + timeStamp + "\njudge: " + player.getName() + "\ntarget: " + target.getName() + "\nparagraph: " + paragraph + "\n");
        
        return false;
    }
}
