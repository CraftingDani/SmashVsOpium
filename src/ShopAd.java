import org.bukkit.Location;

public class ShopAd
{
    public String name;
    public int price;
    public String playerName;

    public ShopAd(String name, int price, String playerName, Location playerLoc)
    {
        this.name = name;
        this.price = price;
        this.playerName = playerName;
    }

    @Override
    public String toString()
    {
        return "§a" + playerName + "§2: §a" + name + ", price§2: " + price + "§a";
    }
}
