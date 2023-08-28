import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class StartProjectCommand implements CommandExecutor
{
    private int countDown;
    public static ItemStack sign;

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
    {
        Player player = (Player) sender;
        
        if(!player.getName().equals("CraftingDani")) return false;

        Bukkit.broadcastMessage("§8§lOpening the server in §210 seconds§a!");
        Bukkit.getWorld("world").setTime(0);

        countDown = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
        {
            int count = 10;

            @Override
            public void run()
            {
                for(Player i : Bukkit.getOnlinePlayers())
                {
                    i.sendTitle(Main.getPlayerTeamColor(i.getName()) + "§l" + count, null, 5, 8, 5);

                    Firework firework = (Firework) i.getWorld().spawn(i.getLocation().add(0, 6, 0), Firework.class);
                    FireworkMeta meta = (FireworkMeta) firework.getFireworkMeta();
                    meta.addEffect(FireworkEffect.builder().withColor(Color.LIME).withFade(Color.GREEN).with(Type.BALL_LARGE).flicker(true).trail(true).build());
                    meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).with(Type.BURST).flicker(true).trail(true).build());
                    meta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).withFade(Color.LIME).with(Type.STAR).flicker(true).trail(true).build());
                    meta.setPower(2);
                    firework.setFireworkMeta(meta);
                }

                if(count <= 0)
                {
                    Bukkit.getWorld("world").getWorldBorder().setSize(29999984, 800 * 60 * 20);
                    Bukkit.broadcastMessage("§a§lLets§2§lGOOO§a!!!");
                    Bukkit.getScheduler().cancelTask(countDown);
                    return;
                }
                count--;
            }
        }, 20, 20);

        return false;
    }
}
