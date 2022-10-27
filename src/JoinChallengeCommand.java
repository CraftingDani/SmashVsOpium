import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class JoinChallengeCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cYou can only use this command as a player.");
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("unitedWorld.joinChallenge"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        if(player.getWorld() == Bukkit.getWorld("Event"))
        {
            player.sendMessage("§cYou do are already in the challenge world.");
            return false;
        }
        
        FileConfiguration config = Main.getPlugin().getConfig();

        player.sendMessage("§aYou have been teleported to the challenge. §2Good luck!");
        config.set("lastLocation." + player.getName(), player.getLocation());
        player.teleport(new Location(Bukkit.getWorld("Event"), 50.5, -60, 50.5, -180, 0));
        player.setGameMode(GameMode.SURVIVAL);

        int timeScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
        {
            int count = 0;
            
            @Override
            public void run()
            {
                config.set("balances." + player.getName(), config.getInt("balances." + player.getName()) + 1);
                Main.getPlugin().saveConfig();
                if(count > 0) player.sendMessage("§aYou earned some money for surviving another five minutes!");
                else          player.sendMessage("§aYou earned some money for surviving five minutes!");
                count++;
                Main.updateScoreboard(player);
            }
        }, 5 * 60 * 20, 5 * 60 * 20);

        config.set("timeScheduler." + player.getName(), timeScheduler);
        
        int spawnScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
        {
            randomAction(5, player);
            
        }, 10 * 20, 20 * 20);
        
        config.set("spawnScheduler." + player.getName(), spawnScheduler);
        Main.getPlugin().saveConfig();

        return false;
    }


    private int random(int amount)
    {
        return (int) Math.round(Math.random() * (amount-1));
    }

    private void randomAction(int amount, Player player)
    {
        int rand = random(amount);

        switch(rand)
        {
            case 0: break;
            
            case 1:
                spawnZombie(player);
                break;
            case 2:
                summonLightning(player);
                break;
            case 3:
                summonEffectcloud(player);
                break;
            case 4:
                summonSkeleton(player);
                break;
        }
    }


    private void spawnZombie(Player player)
    {
        Zombie zombie = (Zombie) player.getWorld().spawnEntity(randomLocation(player.getLocation()), EntityType.ZOMBIE);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        zombie.getEquipment().setItemInMainHand(sword);
        zombie.getEquipment().setItemInOffHand(axe);
        zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        zombie.setAdult();
        zombie.setHealth(6);
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 255, false, false));
    }

    private void summonLightning(Player player)
    {
        Location playerLoc = player.getLocation();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> 
        {
            Bukkit.getWorld("Event").spawnEntity(playerLoc, EntityType.LIGHTNING);
        }, 20);
    }

    private void summonEffectcloud(Player player)
    {
        Location playerLoc = player.getLocation();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> 
        {
            AreaEffectCloud cloud = (AreaEffectCloud) player.getWorld().spawnEntity(playerLoc, EntityType.AREA_EFFECT_CLOUD);
            Particle[] particles = { Particle.DRAGON_BREATH, Particle.END_ROD, Particle.ELECTRIC_SPARK };
            
            cloud.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
            cloud.setParticle(particles[random(3)]);
            
        }, 1 * 20);
    }

    private void summonSkeleton(Player player)
    {
        Skeleton skeleton = (Skeleton) player.getWorld().spawnEntity(randomLocation(player.getLocation()), EntityType.SKELETON);
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 255, false, false));
        skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
        skeleton.getEquipment().setItemInOffHand(new ItemStack(Material.ARROW));
    }


    private Location randomLocation(Location playerLoc)
    {
        int[] randoms = new int[2];
        for(int i = 0; i < randoms.length; i++)
        {
            int rand = random(3);

            switch(rand)
            {
                case 2:
                    randoms[i] = -1;
                    break;
                    
                default:
                    randoms[i] = rand;
            }
        }

        return playerLoc.add(randoms[0], 0, randoms[1]);
    }
}
