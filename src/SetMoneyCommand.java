import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMoneyCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
    {
        Player target = Bukkit.getPlayer(args[0]);

        Main.getPlugin().getConfig().set("balances." + target.getName(), args[1]);
        Main.getPlugin().saveConfig();
        
        Main.updateScoreboard(target);
        return false;
    }
    
}
