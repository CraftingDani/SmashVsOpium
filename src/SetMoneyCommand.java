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
        int amount = Integer.parseInt(args[1]);

        Main.getPlugin().getConfig().set("balances." + target.getName(), amount);
        Main.getPlugin().saveConfig();

        sender.sendMessage("§aYou set " + target.getName() + "'s money to §2§l" + amount);
        target.sendMessage("§2" + sender.getName() + "§a set your money to §2§l" + amount);
        
        Main.updateScoreboard(target);
        return false;
    }
}
