import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MarketCommand implements CommandExecutor
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
        
        if(!sender.hasPermission("svo.market"))
        {
            sender.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        try
        {
            switch(args[0])
            {
                case "add":
                    int price = Integer.parseInt(args[2]);
                    addOffer(player, args[1], price);
                    break;

                case "list":
                    listOffers(player);
                    break;

                case "remove":
                    removeOffer(args[1], player);
                    break;

                default:
                player.sendMessage("§cPlease use /" + label + " list | /" + label + " remove <item> | /" + label + " add <item> <price>");
                    break;
            }
        }
        catch(Exception e)
        {
            player.sendMessage("§cPlease use /" + label + " list | /" + label + " remove <item> | /" + label + " add <item> <price>");
            return false;
        }

        Main.getPlugin().saveConfig();

        return false;
    }


    private void addOffer(Player player, String item, int price)
    {
        List<String> offers = (List<String>) config.getList("marketOffers");
        if(offers == null) offers = new ArrayList<String>();

        if(offers.contains(item))
        {
            player.sendMessage("§cThis item already exists.");
            return;
        }
        
        offers.add(item);

        config.set("marketOffers", offers);
        config.set("marketPlayers." + item, player.getName());
        config.set("marketPrices." + item, price);

        Main.getPlugin().saveConfig();
    }


    private void listOffers(Player player)
    {
        List<String> offers = (List<String>) config.getList("marketOffers");
        if(offers == null) offers = new ArrayList<String>();

        player.sendMessage("§8Market Offers:");
        
        for(String i : offers)
        {
            player.sendMessage("§8- " + config.get("marketPlayers." + i) + ": " + i + " (" + config.getInt("marketPrices." + i) + " Coins)");
        }
    }


    private void removeOffer(String item, Player player)
    {
        List<String> offers = (List<String>) config.getList("marketOffers");
        if(offers == null) offers = new ArrayList<String>();

        if(!config.get("marketPlayers." + item).equals(player.getName())) // test if it is the player's offer
        {
            player.sendMessage("§cThis is not your shop offer.");
            return;
        }

        offers.remove(item);

        config.set("marketOffers", offers);
        config.set("marketPlayers." + item, null);
        config.set("marketPrices." + item, null);
    }
}
