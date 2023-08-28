import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.List;

public class EconomyImplementer implements Economy
{
    private Main plugin = Main.getPlugin();
    FileConfiguration  config = plugin.getConfig();

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName()
    {
        return "SmashVsOpium Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural()
    {
        return "coins";
    }

    @Override
    public String currencyNameSingular()
    {
        return "coin";
    }

    @Override
    public boolean hasAccount(String s) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    //get money

    @Override
    public double getBalance(String s)
    {
        Player player = Bukkit.getPlayer(s);
        return config.getInt("balances." + player.getName());
    }

    @Override
    public double getBalance(OfflinePlayer player)
    {
        return config.getInt("balances." + player.getName());
    }

    @Override
    public double getBalance(String s, String s1)
    {
        Player player = Bukkit.getPlayer(s);
        return config.getInt("balances." + player.getName());
    }

    @Override
    public double getBalance(OfflinePlayer player, String s)
    {
        return config.getInt("balances." + player.getName());
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    //decrease money
    
    @Override
    public EconomyResponse withdrawPlayer(String s, double v)
    {
        Player player = Bukkit.getPlayer(s);
        int oldBalance = config.getInt("balances." + player.getName());
        config.set("balances." + player.getName(), oldBalance-v);
        plugin.saveConfig();
        Main.updateScoreboard(player);

        player.sendMessage(s + v);
        return new EconomyResponse(v, getBalance(player), ResponseType.SUCCESS, "hi1");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double lamaproblama)
    { 
        int oldBalance = config.getInt("balances." + player.getName());
        config.set("balances." + player.getName(), oldBalance-lamaproblama);
        plugin.saveConfig();
        Main.getPlugin().getLogger().info(player + "\n" + "\n" + lamaproblama);
        return new EconomyResponse(lamaproblama, getBalance(player), ResponseType.SUCCESS, "hi2");
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v)
    {
        Player player = Bukkit.getPlayer(s);
        int oldBalance = config.getInt("balances." + player.getName());
        config.set("balances." + player.getName(), oldBalance-v);
        Main.updateScoreboard(player);
        plugin.saveConfig();
        Main.updateScoreboard(player);

        player.sendMessage(s + s1 + v);
        return new EconomyResponse(v, getBalance(player), ResponseType.SUCCESS, "hi3");
    }

    

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String s, double v)
    {
        int oldBalance = config.getInt("balances." + player.getName());
        config.set("balances." + player.getName(), oldBalance-v);
        plugin.saveConfig();
        Player onlinePlayer = (Player) player;
        if(player.isOnline()) Main.updateScoreboard(onlinePlayer);
        Main.getPlugin().getLogger().info(player + "\n" + s + "\n" + v);
        return new EconomyResponse(v, getBalance(player), ResponseType.SUCCESS, "hi4");
    }

    //increase money

    @Override
    public EconomyResponse depositPlayer(String s, double v)
    {
        Player player = Bukkit.getPlayer(s);
        int oldBalance = config.getInt("balances." + player.getName());
        config.set("balances." + player.getName(), oldBalance + v);
        plugin.saveConfig();
        Main.updateScoreboard(player);
        return new EconomyResponse(v, config.getInt("balances." + player.getName()), ResponseType.SUCCESS, "hi5");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double v)
    {
        int oldBalance = config.getInt("balances." + player.getName());
        config.set("balances." + player.getName(), oldBalance + v);
        plugin.saveConfig();
        return new EconomyResponse(v, config.getInt("balances." + player.getName()), ResponseType.SUCCESS, "hi6");
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v)
    {
        Player player = Bukkit.getPlayer(s);
        int oldBalance = config.getInt("balances." + player.getName());
        config.set("balances." + player.getName(), oldBalance + v);
        Main.updateScoreboard(player);
        plugin.saveConfig();
        return new EconomyResponse(v, config.getInt("balances." + player.getName()), ResponseType.SUCCESS, "hi7");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String s, double v)
    {
        int oldBalance = config.getInt("balances." + player.getName());
        config.set("balances." + player.getName(), oldBalance + v);
        plugin.saveConfig();
        return new EconomyResponse(v, config.getInt("balances." + player.getName()), ResponseType.SUCCESS, "hi8");
    }


    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}
