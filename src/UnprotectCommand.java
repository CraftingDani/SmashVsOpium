import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UnprotectCommand implements CommandExecutor
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

        if(!player.hasPermission("svo.protect"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        FileConfiguration config = Main.getPlugin().getConfig();
        Block block = player.getTargetBlock(null, 100);
        
        if(config.getString("protectedchests." + block) == null || !config.getString("protectedchests." + block).equals(player.getName()))
        {
            player.sendMessage("§cYou have not protected this barrel.");
            return false;
        }

        player.sendMessage("§8Unprotecting this barrel...");
        config.set("protectedchests." + block, null);
        Main.getPlugin().saveConfig();

        return false;
    }
}
