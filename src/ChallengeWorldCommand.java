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
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ChallengeWorldCommand implements CommandExecutor
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
        
        if(Main.checkPrison(player.getName()))
        {
            player.sendMessage("§cYou cannot use this command while you are in prison!");
            return false;
        }

        if(!player.hasPermission("svo.challenge"))
        {
            player.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        if(player.getWorld() == Bukkit.getWorld("challenge"))
        {
            leaveChallenge(player);
            return false;
        }

        FileConfiguration config = Main.getPlugin().getConfig();

        player.sendMessage("§8You have been teleported to the challenge world. " + Main.getPlayerTeamColor(player.getName()) + "Good luck!");
        if(player.getWorld() != Bukkit.getWorld("prison")) config.set("lastLocation." + player.getName(), player.getLocation());
        player.teleport(new Location(Bukkit.getWorld("challenge"), 0.5, -58, 0.5, -180, 0));
        player.setGameMode(GameMode.SURVIVAL);

        int challengeTimeScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
        {
            int count = 0;

            @Override
            public void run()
            {
                count++;
                int minutes = (count % 3600) / 60;
                int seconds = count % 60;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Main.getPlayerTeamColor(player.getName()) + minutes + "§8:" + Main.getPlayerTeamColor(player.getName()) + seconds));
            }
        }, 1 * 20, 1 * 20);

        config.set("challengeTimeScheduler." + player.getName(), challengeTimeScheduler);

        int timeScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
        {
            int count = 0;
            
            @Override
            public void run()
            {
                config.set("balances." + player.getName(), config.getInt("balances." + player.getName()) + Main.CHALLENGE_MONEY);
                Main.getPlugin().saveConfig();
                if(count > 0) player.sendMessage("§8You earned some money for surviving another " + Main.CHALLENGE_MONEY_TIME + " minutes!");
                else          player.sendMessage("§8You earned some money for surviving " + Main.CHALLENGE_MONEY_TIME + " minutes!");
                count++;
                Main.updateScoreboard(player);
            }
        }, Main.CHALLENGE_MONEY_TIME * 60 * 20, Main.CHALLENGE_MONEY_TIME * 60 * 20);

        config.set("timeScheduler." + player.getName(), timeScheduler);
        
        int spawnScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
        {
            randomAction(5, player);
        }, 5 * 20, Main.CHALLENGE_EVENT_TIME * 20);
        
        config.set("spawnScheduler." + player.getName(), spawnScheduler);
        Main.getPlugin().saveConfig();

        return false;
    }

    private void leaveChallenge(Player player)
    {
        FileConfiguration config = Main.getPlugin().getConfig();

        Bukkit.getScheduler().cancelTask(config.getInt("timeScheduler." + player.getName()));
        Bukkit.getScheduler().cancelTask(config.getInt("spawnScheduler." + player.getName()));
        Bukkit.getScheduler().cancelTask(config.getInt("challengeTimeScheduler." + player.getName()));
        
        config.set("timeScheduler." + player.getName(), null);
        config.set("spawnScheduler." + player.getName(), null);
        config.set("challengeTimeScheduler." + player.getName(), null);

        player.teleport(config.getLocation("lastLocation." + player.getName()));
        player.sendMessage("§8You left the challenge world.");
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
        zombie.setCanPickupItems(false);
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 255, false, false));
    }

    private void summonLightning(Player player)
    {
        Location playerLoc = player.getLocation();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> 
        {
            player.getWorld().spawnEntity(playerLoc, EntityType.LIGHTNING);
        }, 20);
    }

    private void summonEffectcloud(Player player)
    {
        Location playerLoc = player.getLocation();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> 
        {
            int random = random(4);
            AreaEffectCloud cloud = (AreaEffectCloud) player.getWorld().spawnEntity(playerLoc, EntityType.AREA_EFFECT_CLOUD);
            Particle[] particles = { Particle.DRAGON_BREATH, Particle.END_ROD, Particle.ELECTRIC_SPARK, Particle.ENCHANTMENT_TABLE };
            PotionType[] potionTypes = { PotionType.INSTANT_DAMAGE, PotionType.POISON, PotionType.SLOWNESS, PotionType.WEAKNESS };
            
            cloud.setBasePotionData(new PotionData(potionTypes[random]));
            cloud.setParticle(particles[random]);
            
        }, 1 * 20);
    }

    private void summonSkeleton(Player player)
    {
        Skeleton skeleton = (Skeleton) player.getWorld().spawnEntity(randomLocation(player.getLocation()), EntityType.SKELETON);
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 255, false, false));
        skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
        skeleton.getEquipment().setItemInOffHand(new ItemStack(Material.ARROW));
        skeleton.setCanPickupItems(false);
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
