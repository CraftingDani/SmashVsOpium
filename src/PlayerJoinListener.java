import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerJoinListener implements Listener
{
    FileConfiguration config = Main.getPlugin().getConfig();
    
    @EventHandler
    private void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        if(Main.wartungsarbeiten && !player.getName().equals("CraftingDani") && !player.getName().equals("Legend_Cookie17") && !player.getName().equals("Ater12")) player.kickPlayer("§8§lWartungarbeiten");

        String playerTeamColor = Main.getPlayerTeamColor(player.getName());
        e.setJoinMessage("§8→ " + playerTeamColor + player.getName());
        Main.updateScoreboard(player);
        player.setDisplayName(playerTeamColor + player.getName());
        player.setPlayerListName(playerTeamColor + player.getName());
        player.setPlayerListHeader("§a§lSmash§8Vs§c§lOpium\n\n§8Team " + Main.getPlayerTeamColor(player.getName()) + "§l" + Main.getPlayerTeamName(player.getName()) + "\n");
        player.setPlayerListFooter(" ");
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1000);
        if(config.getLocation("lastLocation." + player.getName()) != null && !Main.checkPrison(player.getName())) player.teleport(config.getLocation("lastLocation." + player.getName()));
        player.setGameMode(GameMode.SURVIVAL);
        player.sendTitle("", "§8§k- §a§lSmash§8Vs§c§lOpium §8§k-", 5, 75, 5);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f§k- §a§lSmash§8Vs§c§lOpium §f§k-"));


       /* try
        {
            FileWriter writer = new FileWriter("joinLog.txt", true); // The 'true' parameter appends to the existing file

            
            writer.write(player.get);
            writer.flush();
            writer.close();
        } catch(Exception ex) { }
        */

        if(player.hasPlayedBefore()) return;

        player.teleport(Main.SPAWN_LOC);
        Bukkit.broadcastMessage("§8§a§lSmash§8Vs§c§lOpium§8, " + playerTeamColor + player.getName() + "§8!");
        Firework firework = (Firework) Bukkit.getWorld("world").spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta meta = (FireworkMeta) firework.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder().withColor(Color.LIME).withFade(Color.GREEN).with(Type.BALL_LARGE).flicker(true).trail(true).build());
        firework.setFireworkMeta(meta);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        String teamColor = Main.getPlayerTeamColor(player.getName());
        e.setQuitMessage("§8← " + teamColor + player.getName());
        if(config.get("timeScheduler." + player.getName()) != null)
        {
            Bukkit.getScheduler().cancelTask(config.getInt("timeScheduler." + player.getName()));
            Bukkit.getScheduler().cancelTask(config.getInt("spawnScheduler." + player.getName()));
            Bukkit.getScheduler().cancelTask(config.getInt("challengeTimeScheduler." + player.getName()));
            config.set("timeScheduler." + player.getName(), null);
            config.set("spawnScheduler." + player.getName(), null);
            config.set("challengeTimeScheduler." + player.getName(), null);
        }
        else
        {
            if(player.getWorld() != Bukkit.getWorld("adminshop") && player.getWorld() != Bukkit.getWorld("vip") && player.getWorld() != Bukkit.getWorld("prison")) config.set("lastLocation." + player.getName(), player.getLocation());
        }

        Main.getPlugin().saveConfig();
    }
}
