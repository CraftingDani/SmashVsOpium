import java.time.LocalDate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PrisonCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args)
    {
        if(!sender.hasPermission("svo.prison"))
        {
            sender.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        if(args.length != 2)
        {
            sender.sendMessage("§cPlease use /" + label + " <player> <yyyy-mm-dd>");
            return false;
        }

        Player target;
        LocalDate date;
        
        try
        {
            target = Bukkit.getPlayer(args[0]);
            
            date = LocalDate.parse(args[1]);
        }
        catch(Exception e)
        {
            sender.sendMessage("§cPlease use /" + label + " <player> <yyyy-mm-dd>");
            return false;
        }

        FileConfiguration config = Main.getPlugin().getConfig();
        
        target.teleport(new Location(Bukkit.getWorld("prison"), 69, 69, 69));
        target.setGameMode(GameMode.ADVENTURE);
        config.set("prisonPlayers." + target.getName(), date.toString());
        Main.getPlugin().saveConfig();

        sender.sendMessage("§8You sent " + target.getName() + " to the prison.");

        return false;
    }
}
