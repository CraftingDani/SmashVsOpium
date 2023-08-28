import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageListener implements Listener
{
    @EventHandler
    private void onDamage(EntityDamageEvent e)
    {
        if(e.getEntityType() != EntityType.PLAYER) return;

        Player player = (Player) e.getEntity();
        FileConfiguration config = Main.getPlugin().getConfig();

        if(config.getBoolean("spawnProtection." + player.getName())) e.setCancelled(true);


        if(e.getCause() != DamageCause.FALL) return;

        Location start = new Location(Bukkit.getWorld("world"), 940, 100, 861);
        Location end = new Location(Bukkit.getWorld("world"), 970, 100, 891);
        Block block = e.getEntity().getLocation().getBlock();

        if(block.getX() <= end.getX() && block.getX() >= start.getX()
        && block.getZ() <= end.getZ() && block.getZ() >= start.getZ())
        {
            e.setCancelled(true);
        }
    }
}
