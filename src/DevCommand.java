import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DevCommand implements CommandExecutor
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

        if(!player.hasPermission("unitedworld.dev"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }


        String displayName = player.getDisplayName();

        if(displayName.contains("§8[dev] "))
        {
            //TODO rest

            player.setDisplayName(displayName.replace("§8[dev] ", ""));
            player.setPlayerListName(displayName.replace("§8[dev] ", ""));
            return false;
        }

        player.setDisplayName("§8[dev] " + displayName);
        player.setPlayerListName("§8[dev] " + displayName);


        return false;
    }
}
