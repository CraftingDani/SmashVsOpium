import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Main extends JavaPlugin
{
    private static Main plugin;
    public static Scoreboard board;
    public static final int HEART_MONEY = 6;

    @Override
    public void onEnable()
    {
        getLogger().log(Level.INFO, "UnitedWorld enabled!");
        plugin = this;
        initCommands();
        initEvents();
    }

    @Override
    public void onDisable()
    {
        saveDefaultConfig();
        
        for(Player player : Bukkit.getOnlinePlayers())
        {
            try
            {
                Bukkit.getScheduler().cancelTask(getConfig().getInt("timeScheduler." + player.getName()));
                Bukkit.getScheduler().cancelTask(getConfig().getInt("spawnScheduler." + player.getName()));
            }
            catch(Exception e) { }
        }
    }


    private void initCommands()
    {
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("setmoney").setExecutor(new SetMoneyCommand());
        getCommand("joinchallenge").setExecutor(new JoinChallengeCommand());
        getCommand("leavechallenge").setExecutor(new LeaveChallengeCommand());
        getCommand("ec").setExecutor(new EcCommand());
        getCommand("startproject").setExecutor(new StartProjectCommand()); // animate border
        getCommand("buyheart").setExecutor(new BuyHeartCommand()); // buy heart after negative money
    }

    private void initEvents()
    {
        Bukkit.getPluginManager().registerEvents(new ServerPingListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatMessageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlaverMoveListener(), this);
    }

    public static void updateScoreboard(Player player)
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("UnitedWorldScoreboard", "null", "null");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§a§lUnited§2§lWorld");
        objective.getScore(" ").setScore(3);
        objective.getScore("§amoney§2:").setScore(2);
        objective.getScore("§f " + getPlugin().getConfig().getString("balances." + player.getName())).setScore(1);
        objective.getScore("  ").setScore(0);

        player.setScoreboard(board);
    }

    public void checkNegativeMoney(Player player)
    {
        int balance = getConfig().getInt("balances." + player.getName());
        if(balance < 0)
        {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - balance * -1);
            getConfig().set("balances." + player.getName(), 0);
            player.sendMessage("§cYou lost §4" + balance * -1 + " heart(s) §cbecause you have a negative amount of money.");
            updateScoreboard(player);
            saveConfig();
        }
    }

    public static Main getPlugin()
    {
        return plugin;
    }
}



//-- TODO --\\

// - start animation, countdown
 