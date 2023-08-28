import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class PullBowListener implements Listener
{
    //method copyed from "Troll Boss", decompiled by Procyon v0.5.36

    @EventHandler
    private void onPullBowHit(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Projectile)
        {
            final Projectile a = (Projectile) e.getDamager();
            if(a instanceof Arrow)
            {
                final Arrow ar = (Arrow) a;
                final ProjectileSource shooter = ar.getShooter();
                if(shooter instanceof Player)
                {
                    final Player p = (Player) shooter;
                    if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR && ("§8§lPull Bow".equalsIgnoreCase(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName())))
                    {
                        e.setCancelled(true);
                        final Location pulled = e.getEntity().getLocation();
                        final Location to = p.getLocation();
                        final Vector direction = to.toVector().subtract(pulled.toVector()).normalize();
                        direction.multiply(3.5);
                        direction.setY(0.9);
                        e.getEntity().setVelocity(direction);
                        a.remove();
                        //this.plugin.addBowStats("Pull");
                    }
                }
            }
        }
    }

}
