import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor
{
    FileConfiguration config = Main.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args)
    {
        /*if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }*/

        Player player = (Player) sender;

        if(!player.hasPermission("svo.shop"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        if(args[0].equals("list"))
        {
            listAds(player);
            return false;
        }

        if(args[0].equals("remove"))
        {
            removeAd(player, args[1]);
            return false;
        }

        addAd(player, args);
        
        return false;
    }


    private void addAd(Player player, String[] args)
    {
        String name;
        int price;

        try
        {
            name = args[1];
            price = Integer.parseInt(args[2]);
            config.set("shopOffers", config.getStringList("shopOfers").add(player.getName() + " " + name + " " + price));
        }
        catch (Exception e)
        {
            player.sendMessage("§cPlease use /shop add <name> <price>!");
        }

        Main.getPlugin().saveConfig();
    }


    private void listAds(Player player)
    {
        player.sendMessage(config.getString("shopOffers.*.name"));
    }


    private void removeAd(Player player, String name)
    {

    }
}
