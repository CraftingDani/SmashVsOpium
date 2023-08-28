import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LeaveChallengeCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("svo.leavechallenge"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        FileConfiguration config = Main.getPlugin().getConfig();

        if(config.get("timeScheduler." + player.getName()) == null)
        {
            player.sendMessage("§cYou are not in the challenge world!");
            return false;
        }

        Bukkit.getScheduler().cancelTask(config.getInt("timeScheduler." + player.getName()));
        Bukkit.getScheduler().cancelTask(config.getInt("spawnScheduler." + player.getName()));
        Bukkit.getScheduler().cancelTask(config.getInt("challengeTimeScheduler." + player.getName()));
        
        config.set("timeScheduler." + player.getName(), null);
        config.set("spawnScheduler." + player.getName(), null);
        config.set("challengeTimeScheduler." + player.getName(), null);

        player.teleport(config.getLocation("lastLocation." + player.getName()));
        player.sendMessage("§aYou left the challenge.");

        return false;
    }
}
