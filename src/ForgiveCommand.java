import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ForgiveCommand implements CommandExecutor
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

        if(!player.hasPermission("svo.forgive"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        if(args.length != 1)
        {
            player.sendMessage("§cPlease use /" + label + " <player>");
            return false;
        }

        FileConfiguration config = Main.getPlugin().getConfig();
        OfflinePlayer target;
        target = Bukkit.getPlayer(args[0]);
        
        if(target == null)
        {
            player.sendMessage("§cPlayer not found.");
            return false;
        }

        if(config.getString("hasKilled." + target.getName()) == null || !config.getString("hasKilled." + target.getName()).equals(player.getName())) //has killed thie target, same team
        {
            player.sendMessage("§cThis player has not killed you.");
            return false;
        }

        if(!Main.getPlayerTeamName(player.getName()).equals(Main.getPlayerTeamName(target.getName())))
        {
            player.sendMessage("§cThis player is not a member of your team.");
            return false;
        }

        config.set("hasKilled." + target.getName(), null);
        Main.getPlugin().saveConfig();
        player.sendMessage("§8You have forgiven " + target.getName() + ".");
        if(target.isOnline()) ((Player) target).sendMessage("§8" + player.getName() + " has forgiven you.");
        
        return false;
    }
}
