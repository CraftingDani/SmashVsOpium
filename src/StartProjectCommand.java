import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class StartProjectCommand implements CommandExecutor
{
    int countDown;

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
    {
        Player player = (Player) sender;
        
        if(!player.getName().equals("CraftingDani")) return false;


        Bukkit.broadcastMessage("§aWelcome to §lUnited§2§lWorld§a!\nOpening the server in §210 seconds§a!");

        countDown = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
        {
            int count = 10;

            @Override
            public void run()
            {
                player.sendTitle("§a§l" + count, null, 5, 8, 5);

                if(count <= 0)
                {
                    for(Player i : Bukkit.getOnlinePlayers())
                    {
                        Firework firework = (Firework) i.getWorld().spawn(player.getLocation(), Firework.class);
                        FireworkMeta meta = (FireworkMeta) firework.getFireworkMeta();
                        meta.addEffect(FireworkEffect.builder().withColor(Color.LIME).withFade(Color.GREEN).with(Type.BALL_LARGE).flicker(true).trail(true).build());
                        meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).with(Type.BURST).flicker(true).trail(true).build());
                        meta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).withFade(Color.LIME).with(Type.STAR).flicker(true).trail(true).build());
                        meta.setPower(1);
                        firework.setFireworkMeta(meta);
                    }
                    
                    stopScheduler();
                    return;
                }

                count--;
            }
            
        }, 20, 20);



        return false;
    }


    private void stopScheduler()
    {
        Bukkit.getScheduler().cancelTask(countDown);
    }
}
