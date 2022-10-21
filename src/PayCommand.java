import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("UnitedWorld.pay"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }
        
        FileConfiguration config = Main.getPlugin().getConfig();
        int balance = Integer.parseInt(config.getString("balances." + player.getName()));
        int amount;
        Player target;

        try
        {
            amount = Integer.parseInt(args[1]);
            target = Bukkit.getPlayer(args[0]);
        }
        catch(Exception e)
        {
            player.sendMessage("§cPlease use §2/" + label + " <player> <amount>§c!");
            return false;
        }
        
        if(target == null)
        {
            player.sendMessage("§cThis player is not online!");
            return false;
        }

        if(amount < 0)
        {
            player.sendMessage("§cYou are not allowed to pay negative money!");
            return false;
        }

        if((balance - amount) < 0)
        {
            player.sendMessage("§cYou do not have enough money!");
            return false;
        }

        config.set("balances." + player.getName(), balance - amount);
        config.set("balances." + target.getName(), config.getInt("balances." + target.getName()) + amount);

        Main.updateScoreboard(player);
        Main.updateScoreboard(target);

        player.sendMessage("§aYou payed §2§l" + amount + " §ato §2" + target.getName() + "§a.");
        target.sendMessage("§2" + player.getName() + " §apayed you §2§l" + amount + "§a.");

        return false;
    }
}
