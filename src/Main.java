import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
    public static final Location SPAWN_LOC = new Location(Bukkit.getWorld("world"), 955, 91, 876);
    public static final Location TEAM_GREEN_LOC = new Location(Bukkit.getWorld("world"), 954.5, 70, 976.5);
    public static final Location TEAM_RED_LOC = new Location(Bukkit.getWorld("world"), 955.5, 76, 776.5);
    public static final int HEART_MONEY = 3;
    public static final int EXTRAHEART_MONEY = 10;
    public static final int KILL_MONEY = 3;
    public static final int DEATH_MONEY = 3;
    public static final int CHALLENGE_MONEY = 3;
    public static final int PROTECT_MONEY = 8;
    public static final int CHALLENGE_MONEY_TIME = 3;    // in m
    public static final int CHALLENGE_EVENT_TIME = 10;   // in s
    public static final int EXP_PROT_MONEY = 10;
    public static final int ACHIEVEMENT_MONEY = 2;
    public static final String TEAM_GREEN_NAME = "Smash";
    public static final String TEAM_RED_NAME = "Opium";
    public static final ArrayList<String> TEAM_GREEN_PLAYERS = new ArrayList<String>();
    public static final ArrayList<String> TEAM_RED_PLAYERS = new ArrayList<String>();
    public static boolean wartungsarbeiten = false;
    public static String[] adminNames = {"Ater12", "CraftingDani", "Legend_Cookie17"};

    public EconomyImplementer economyImplementer;
    private VaultHook vaultHook;



    @Override
    public void onEnable()
    {
        getLogger().log(Level.INFO, "SmashVsOpium enabled!");

        plugin = this;

        addPlayers();
        initEvents();
        initCommands();
        resetGhostBlocks();
        particles();

        for(Player i : Bukkit.getOnlinePlayers())
        {
            updateScoreboard(i);
        }
        //setupEco();
    }

    @Override
    public void onDisable()
    {
        getLogger().log(Level.INFO, "SmashVsOpium disabled!");
        saveDefaultConfig();
        
        for(Player i : Bukkit.getOnlinePlayers())
        {
            try
            {
                Bukkit.getScheduler().cancelTask(getConfig().getInt("timeScheduler." + i.getName()));
                Bukkit.getScheduler().cancelTask(getConfig().getInt("spawnScheduler." + i.getName()));
                Bukkit.getScheduler().cancelTask(getConfig().getInt("challengeTimeScheduler." + i.getName()));
                
                getConfig().set("timeScheduler." + i.getName(), null);
                getConfig().set("spawnScheduler." + i.getName(), null);
                getConfig().set("challengeTimeScheduler." + i.getName(), null);

                i.teleport(getConfig().getLocation("lastlocation." + i.getName()));
            }
            catch(Exception e) { }
        }
        //vaultHook.unhook();
    }



    private void initCommands()
    {
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("setmoney").setExecutor(new SetMoneyCommand());
        getCommand("challengeworld").setExecutor(new ChallengeWorldCommand());
        getCommand("ec").setExecutor(new EcCommand());
        getCommand("startproject").setExecutor(new StartProjectCommand()); // animate border
        getCommand("buyheart").setExecutor(new BuyHeartCommand()); // buy heart after negative money
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("protect").setExecutor(new ProtectCommand());
        getCommand("unprotect").setExecutor(new UnprotectCommand());
        getCommand("extraheart").setExecutor(new ExtraHeartCommand());
        getCommand("adminshop").setExecutor(new AdminshopCommand());
        getCommand("forgive").setExecutor(new ForgiveCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("vip").setExecutor(new VipCommand());
        getCommand("prison").setExecutor(new PrisonCommand());
        getCommand("dish").setExecutor(new DishCommand());
        getCommand("market").setExecutor(new MarketCommand());
        getCommand("hsvwehs").setExecutor(new HsvwehsCommand());
        getCommand("dev").setExecutor(new DevCommand());
        getCommand("wartungsarbeiten").setExecutor(new WartungarbeitenCommand());
    }

    private void initEvents()
    {
        Bukkit.getPluginManager().registerEvents(new ServerPingListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatMessageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlaverMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlaverInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityExplosionListener(), this);
        Bukkit.getPluginManager().registerEvents(new HopperListener(), this);
        Bukkit.getPluginManager().registerEvents(new AdvancementListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PullBowListener(), this);
        Bukkit.getPluginManager().registerEvents(new DropItemListener(), this);
    }

    public static void updateScoreboard(Player player)
    {
        String playerTeamColor = getPlayerTeamColor(player.getName());
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("SmashVsOpiumScoreboard", "null", "null");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§a§lSmash§8Vs§c§lOpium");
        objective.getScore(" ").setScore(3);
        objective.getScore("§8money:").setScore(2);
        objective.getScore(" " + playerTeamColor + getPlugin().getConfig().getInt("balances." + player.getName())).setScore(1);
        objective.getScore("  ").setScore(0);

        player.setScoreboard(board);
    }

    public void checkNegativeMoney(Player player)
    {
        int balance = getConfig().getInt("balances." + player.getName());

        if(balance < 0)
        {
            getConfig().set("balances." + player.getName(), 0);
            if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < 1) return;
            
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - balance * -1);
            player.sendMessage("§cYou lost §4" + balance * -1 + " heart(s) §cbecause you have a negative amount of money.");
            updateScoreboard(player);
            saveConfig();
        }
    }

    private void addPlayers()
    {
        TEAM_GREEN_PLAYERS.add("CraftingDani");
        TEAM_GREEN_PLAYERS.add("Ater12");
        TEAM_GREEN_PLAYERS.add("Legend_Cookie17");
        TEAM_GREEN_PLAYERS.add("CrafterJarny");
        TEAM_GREEN_PLAYERS.add("HerrGecko");
        TEAM_GREEN_PLAYERS.add("8anders");
        TEAM_GREEN_PLAYERS.add("Crafterbig");

        TEAM_RED_PLAYERS.add("JJ138");
        TEAM_RED_PLAYERS.add("AllMovie636");
        TEAM_RED_PLAYERS.add("AimL");
        TEAM_RED_PLAYERS.add("Mrdn_in_Paris");
        TEAM_RED_PLAYERS.add("MoiNoah");
    }

    private void resetGhostBlocks()
    {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                updateGhostBlocks();
            }
        }, 0 * 20, 20 * 20);
    }

    public void updateGhostBlocks()
    {
        List<Entity> entities = Bukkit.getWorld("world").getEntities();
        List<Location> blocks = (List<Location>) getConfig().getList("wtBlocks");
        if(blocks == null) blocks = new ArrayList<Location>();

        for (Location i : blocks)
        {
            String material = getConfig().getString("wtBlockMaterials." + i);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "summon falling_block " + i.getBlockX() + " " + i.getBlockY() + " " + i.getBlockZ() +  " {BlockState:{Name:\"minecraft:" + material + "\"},NoGravity:1b,Time:1,DropItem:0b}");
        }

        for (Entity i : entities)
        {
            if(i.getType() == EntityType.FALLING_BLOCK) i.remove();
        }
    }

    private void particles()
    {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                Bukkit.getWorld("world").spawnParticle(Particle.DUST_COLOR_TRANSITION, TEAM_GREEN_LOC, 50, 0.5, 0, 0.5, new Particle.DustTransition(Color.LIME, Color.WHITE, 1));
                Bukkit.getWorld("world").spawnParticle(Particle.DUST_COLOR_TRANSITION, TEAM_RED_LOC, 50, 0.5, 0, 0.5, new Particle.DustTransition(Color.RED, Color.WHITE, 1));
                
                ghostBLockParticles();
                invPlateParticles();
            }
        }, 10, 10);
    }

    private void ghostBLockParticles()
    {
        List<Location> blocks = (List<Location>) getConfig().getList("wtBlocks");
        if(blocks == null || blocks.size() < 1) return;
        for(Location block : blocks)
        {
            for(String name : Main.getPlayerTeamMembers((getConfig().getString("wtBlockPlayers." + block))))
            {
                Player player = Bukkit.getPlayer(name);
                if(player == null) continue;
                double x = block.getX() + 0.5;
                double y = block.getY() + 0.5;
                double z = block.getZ() + 0.5;
                player.spawnParticle(Particle.DUST_COLOR_TRANSITION, x, y, z, 128, 0.25, 0.25, 0.25, new Particle.DustTransition(Color.GRAY, Color.WHITE, 1));
            }
        }
    }

    private void invPlateParticles()
    {
        List<Location> plates = (List<Location>) getConfig().getList("invPlates");
        if(plates == null || plates.size() < 1) return;
        for(Location plate : plates)
        {
            for(String name : Main.getPlayerTeamMembers((getConfig().getString("invPlatePlayers." + plate))))
            {
                Player player = Bukkit.getPlayer(name);
                if(player == null) continue;
                double x = plate.getX() + 0.5;
                double y = plate.getY() + 0.1;
                double z = plate.getZ() + 0.5;
                player.spawnParticle(Particle.END_ROD, x, y, z, 1, 0.2, 0, 0.2, 0.001);
            }
        }
    }

    private void setupEco()
    {
        economyImplementer = new EconomyImplementer();
        vaultHook = new VaultHook();
        vaultHook.hook();
    }

    public static String getPlayerTeamColor(String playerName)
    {
        if(TEAM_GREEN_PLAYERS.contains(playerName)) return "§a";
        else return "§c";
    }

    public static String getPlayerTeamName(String playerName)
    {
        if(TEAM_GREEN_PLAYERS.contains(playerName)) return TEAM_GREEN_NAME;
        else return TEAM_RED_NAME;
    }

    public static String getPlayerOpponentTeamName(String playerName)
    {
        if(TEAM_GREEN_PLAYERS.contains(playerName)) return TEAM_RED_NAME;
        else return TEAM_GREEN_NAME;
    }

    public static ArrayList<String> getPlayerTeamMembers(String playerName)
    {
        if(TEAM_GREEN_PLAYERS.contains(playerName)) return TEAM_GREEN_PLAYERS;
        else return TEAM_RED_PLAYERS;
    }

    public boolean checkInbetween(Location loc1, Location loc2, Location block)
    {
        return (block.getBlockX() >= Math.min(loc1.getBlockX(), loc2.getBlockX())
        && block.getBlockX() <= Math.max(loc1.getBlockX(), loc2.getBlockX())
        && block.getBlockY() >= Math.min(loc1.getBlockY(), loc2.getBlockY())
        && block.getBlockY() <= Math.max(loc1.getBlockY(), loc2.getBlockY())
        && block.getBlockZ() >= Math.min(loc1.getBlockZ(), loc2.getBlockZ())
        && block.getBlockZ() <= Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
    }

    public boolean isWithinRange(Location blockLoc, Location centerLoc, int range)
    {
        double distance = blockLoc.distance(centerLoc);
        return distance <= range;
    }

    public static boolean checkPrison(String playerName)
    {
        boolean isInPrison = getPlugin().getConfig().getString("prisonPlayers." + playerName) != null;

        if(isInPrison) // delete if expiered
        {
            if(!LocalDate.now(ZoneId.of("Europe/Berlin")).isBefore(LocalDate.parse(getPlugin().getConfig().getString("prisonPlayers." + playerName))))
            {
                getPlugin().getConfig().set("prisonPlayers." + playerName, null);
                getPlugin().saveConfig();
                isInPrison = false;
            }
        }

        return isInPrison;
    }


    public void logToFile(String fileName, String message)
    {
        try
        {
            File dataFolder = getDataFolder();

            if(!dataFolder.exists())
            {
                dataFolder.mkdir();
            }
 
            File saveTo = new File(getDataFolder(), fileName);

            if (!saveTo.exists())
            {
                saveTo.createNewFile();
            }
 
            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
 
            pw.println(message);
            pw.flush();
            pw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static Main getPlugin()
    {
        return plugin;
    }
}
