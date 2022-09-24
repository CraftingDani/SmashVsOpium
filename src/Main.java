import org.bukkit.Bukkit;
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

    @Override
    public void onEnable()
    {
        Bukkit.broadcastMessage("UnitedWorld");
        plugin = this;
        initCommands();
        initEvents();
    }

    @Override
    public void onDisable()
    {
        saveDefaultConfig();
    }


    private void initCommands()
    {
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("setmoney").setExecutor(new SetMoneyCommand());
    }

    private void initEvents()
    {
        Bukkit.getPluginManager().registerEvents(new ServerPingListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatMessageListener(), this);
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
        objective.getScore("§f" + getPlugin().getConfig().getString("balances." + player.getName())).setScore(1);
        objective.getScore("  ").setScore(0);

        player.setScoreboard(board);
    }


    public static Main getPlugin()
    {
        return plugin;
    }
}



//-- TODO --\\

// - challenge system
// - 
