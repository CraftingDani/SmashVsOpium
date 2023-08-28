import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import net.md_5.bungee.api.chat.hover.content.Item;

public class AdminshopCommand implements CommandExecutor
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

        if(!player.hasPermission("svo.adminshop"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }


        /*

        if(args.length == 0)
        {
            player.sendMessage("§8Adminshop Items:\n- ghost_stone\n- ghost_dirt\n- ghost_grass\n- ghost_cobblestone\n- ghostblock_remover\n- invisible_pressureplate\n- invisible_pressureplate_remover\n- explosion_protector\n- light\n- bundle");
            return false;
        }


        String itemName = args[0];

        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        int price = 0;

        switch(itemName)
        {
            case "ghost_stone":
                meta.setDisplayName("§8§lGhost Stone");
                item.setItemMeta(meta);
                price = 10;
                break;

            case "ghost_dirt":
                meta.setDisplayName("§8§lGhost Dirt");
                item.setItemMeta(meta);
                price = 10;
                break;

            case "ghost_grass":
                meta.setDisplayName("§8§lGhost Grass Block");
                item.setItemMeta(meta);
                price = 10;
                break;

            case "ghost_cobblestone":
                meta.setDisplayName("§8§lGhost Grass Cobblestone");
                item.setItemMeta(meta);
                price = 10;
                break;

            case "ghostblock_remover":
                meta.setDisplayName("§8§lGhost Block Remover");
                item.setItemMeta(meta);
                price = 10;
                break;

            case "invisible_pressureplate":
                meta.setDisplayName("§8§lInvisible Pressure Plate");
                item.setItemMeta(meta);
                price = 10;
                break;

            case "invisible_pressureplate_remover":
                meta.setDisplayName("§8§lInvisible Pressure Plate Remover");
                item.setItemMeta(meta);
                price = 10;
                break;
                
            case "explosion_protector":
                meta.setDisplayName("§8§lExplosion Protector");
                item.setItemMeta(meta);
                price = 10;
                break;
            
            case "light":
                item.setType(Material.LIGHT);
                price = 10;
                break;

            case "bundle":
                item.setType(Material.BUNDLE);
                price = 10;
                break;

            default:
                player.sendMessage("§cPlease use /" + label + " <item>.");
                return false;
        }

        buyItem(player, item, price, itemName);

        */

        player.openInventory(createShopInventory());

        return false;
    }


    private void buyItem(Player player, ItemStack item, int price, String itemName)
    {
        FileConfiguration config = Main.getPlugin().getConfig();
        int balance = config.getInt("balances." + player.getName());
        if(balance < price)
        {
            player.sendMessage("§cYou do not have enough money. §8Price: " + price);
            return;
        }

        player.sendMessage("§8You bought a " + itemName + " for " + price + " coins.");

        player.getInventory().addItem(item);
        config.set("balances." + player.getName(), balance - price);
        Main.getPlugin().saveConfig();
        Main.updateScoreboard(player);
    }


    private Inventory createShopInventory()
    {
        Inventory inv = Bukkit.createInventory(null, 6*9, "§a§lAdmin§c§lShop");

        ItemStack spacer = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta spacerMeta = spacer.getItemMeta();
        spacerMeta.setDisplayName(" ");
        spacer.setItemMeta(spacerMeta);

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.setDisplayName("§8§lPull Bow");
        bow.setItemMeta(bowMeta);
        bow.setAmount(10); // BOW PRICE
        
        ItemStack spawnerPick = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta pickMeta = spawnerPick.getItemMeta();
        pickMeta.setDisplayName("§8§lSpawner Breaker");
        ArrayList<String> pickLore = new ArrayList<String>();
        pickLore.add("remaining uses:");
        pickLore.add("3");
        pickMeta.setLore(pickLore);
        spawnerPick.setItemMeta(pickMeta);
        spawnerPick.setAmount(20); // PICK PRICE

        inv.setItem(1, createItem(Material.STICK, "§8§lGhost Stone", 1));
        inv.setItem(2, createItem(Material.STICK, "§8§lGhost Dirt", 1));
        inv.setItem(3, createItem(Material.STICK, "§8§lGhost Grass Block", 1));
        inv.setItem(4, createItem(Material.STICK, "§8§lGhost Cobblestone", 1));
        inv.setItem(7, createItem(Material.STICK, "§8§lGhost Block Remover", 1));
        inv.setItem(12, createItem(Material.STICK, "§8§lInvisible Pressure Plate", 3));
        inv.setItem(14, createItem(Material.STICK, "§8§lInvisible Pressure Plate Remover", 1));
        inv.setItem(22, createItem(Material.STICK, "§8§lExplosion Protector", 7));
        inv.setItem(30, createItem(Material.LIGHT, "§8§lLight", 2));
        inv.setItem(32, createItem(Material.BUNDLE, "§8§lBundle", 3));
        inv.setItem(39, bow);
        inv.setItem(41, spawnerPick);
        inv.setItem(45, createEnchantedBook(Enchantment.MENDING, 1, 10));
        inv.setItem(46, createEnchantedBook(Enchantment.DAMAGE_ALL, 5, 10));
        inv.setItem(47, createEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 4, 10));
        inv.setItem(48, createEnchantedBook(Enchantment.DURABILITY, 3, 10));
        inv.setItem(49, createEnchantedBook(Enchantment.LOOT_BONUS_MOBS, 3, 10));



        return inv;
    }



    private ItemStack createItem(Material material, String name, int price)
    {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        item.setItemMeta(meta);
        item.setAmount(price);

        return item;
    }

    private ItemStack createEnchantedBook(Enchantment enchantment, int level, int price)
    {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.addStoredEnchant(enchantment, level, false);
        book.setItemMeta(meta);
        book.setAmount(price);
        return book;
    }
}
