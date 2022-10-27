import org.bukkit.Bukkit;
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
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("unitedWorld.shop"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        player.sendMessage(args[0]);

        if(args[0].equals("list"))
        {
            listShops(player);
            return false;
        }

        String name;
        int price;

        try
        {
            name = args[1];
            price = Integer.parseInt(args[2]);
        }
        catch (Exception e)
        {
            player.sendMessage("§cPlease use /" + label + " add <name> <price>!");
            return false;
        }
        
        Bukkit.broadcastMessage("added " + name + ", " + price);

        ShopAd ad = new ShopAd(name, price, player);

        config.set("shopAds." + (config.getInt("shopAdCount") +1), ad); // add ad
        config.set("shopAdCount", config.getInt("shopAdCount") +1); // increment count

        Main.getPlugin().saveConfig();

        return false;
    }


    private void listShops(Player player)
    {
        for(int i = 0; i < config.getInt("shopAdCount"); i++)
        {
            player.sendMessage(config.get("shopAds." + (i+1)) + "");
        }
    }
}
