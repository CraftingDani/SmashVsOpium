import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetMoneyCommand implements CommandExecutor
{
    FileConfiguration config = Main.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
    {
        if(!sender.hasPermission("svo.setmoney"))
        {
            sender.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        int amount;

        try { amount = Integer.parseInt(args[1]); }
        catch(Exception e) { return false; }
        
        if(args[0].equals("everyone"))
        {
            setMoneyForEveryone(amount, sender);
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        
        config.set("balances." + target.getName(), amount);
        Main.getPlugin().saveConfig();

        sender.sendMessage("§8You set " + target.getName() + "'s balance to " + amount + ".");
        target.sendMessage("§8" + sender.getName() + " set your balance to " + amount + ".");
        
        Main.updateScoreboard(target);
        return false;
    }

    private void setMoneyForEveryone(int amount, CommandSender sender)
    {
        for(Player i : Bukkit.getOnlinePlayers())
        {
            config.set("balances." + i.getName(), amount);
            
            i.sendMessage("§8" + sender.getName() + " set your money to " + amount + ".");
            
            Main.getPlugin().saveConfig();
            Main.updateScoreboard(i);
        }

        sender.sendMessage("§8You set everyone's money to " + amount + ".");
    }
}
