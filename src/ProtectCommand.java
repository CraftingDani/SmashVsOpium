import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ProtectCommand implements CommandExecutor
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

        if(!player.hasPermission("svo.protect"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        FileConfiguration config = Main.getPlugin().getConfig();
        Block block = player.getTargetBlock(null, 100);
        
        if(block.getType() != Material.BARREL)
        {
            player.sendMessage("§cYou have to look at a barrel to protect it.");
            return false;
        }

        if(player.getWorld() != Bukkit.getWorld("world"))
        {
            player.sendMessage("§cYou can only protect barrels in the regular world.");
            return false;
        }

        if(config.getString("protectedchests." + block) != null)
        {
            player.sendMessage("§cThis barrel has already been protected by §4" + config.getString("protectedchests." + block) + "§c.");
            return false;
        }

        if(config.getInt("balances." + player.getName()) < Main.PROTECT_MONEY)
        {
            player.sendMessage("§cYou do not have enough money to protect a barrel.");
            return false;
        }

        final Location clickedLoc = block.getLocation();
        final Location greenLoc = new Location(Bukkit.getWorld("world"), 954.5, 70, 976.5);
        final Location redLoc = new Location(Bukkit.getWorld("world"), 955.5, 76, 776.5);
        
        //if(Main.getPlugin().isWithinRange(block.getLocation(), Main.TEAM_GREEN_LOC, 20) || Main.getPlugin().isWithinRange(block.getLocation(), Main.TEAM_RED_LOC, 20))
        if(clickedLoc.distance(greenLoc) <= 20 || clickedLoc.distance(redLoc) <= 20)
        {
            player.sendMessage("§cYou are not allowed to do this within 20 blocks of a team's base.");
            return false;
        }
        
        config.set("balances." + player.getName(), config.getInt("balances." + player.getName()) - Main.PROTECT_MONEY);
        config.set("protectedchests." + block, player.getName());
        player.sendMessage("§8Protecting this barrel for " + Main.getPlayerTeamColor(player.getName()) + Main.PROTECT_MONEY + " Coins§8.");
        Main.getPlugin().checkNegativeMoney(player);
        Main.getPlugin().saveConfig();
        Main.updateScoreboard(player);

        return false;
    }
}
