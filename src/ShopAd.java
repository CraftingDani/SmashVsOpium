import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ShopAd
{
    public String name;
    public int price;
    public String playerName;
    public Location playerLoc;

    public ShopAd(String name, int price, Player player)
    {
        this.name = name;
        this.price = price;
        this.playerName = player.getName();
        this.playerLoc = player.getLocation();
    }

    @Override
    public String toString()
    {
        return "§a" + playerName + "§2: §a" + name + ", price§2: " + price + "§a" + ", location§2: " + playerLoc.getBlock().getX() + " " + playerLoc.getBlock().getY() + " " + playerLoc.getBlock().getZ();
    }
}
