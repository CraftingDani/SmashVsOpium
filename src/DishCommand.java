import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DishCommand implements CommandExecutor
{
    ArrayList<String> logs = new ArrayList<String>();
    
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args)
    {
        if(!sender.hasPermission("svo.dish"))
        {
            sender.sendMessage("§cYou do not have the permission to use this command.");
            return false;
        }

        if(args.length != 11)
        {
            sender.sendMessage("§cYou messed up.");
            return false;
        }

        OfflinePlayer accused;       // Angeklagter
        OfflinePlayer prosecutor;    // Kläger
        int           deposit;       // Extrazahlung
        OfflinePlayer lawyer1;       // Anwalt 1
        int           lawyer1Salary; // Anwalt 1 gehalt
        OfflinePlayer lawyer2;       // Anwalt 2
        int           lawyer2Salary; // Anwalt 2 gehalt
        OfflinePlayer judge1;        // Richter 1
        OfflinePlayer judge2;        // Richter 2
        OfflinePlayer judge3;        // Richter 3
        OfflinePlayer judge4;        // Richter 4


        try
        {
            accused = Bukkit.getOfflinePlayer(args[0]);
            prosecutor = Bukkit.getOfflinePlayer(args[1]);
            deposit = Integer.parseInt(args[2]);
            lawyer1 = Bukkit.getOfflinePlayer(args[3]);
            lawyer1Salary = Integer.parseInt(args[4]);
            lawyer2 = Bukkit.getOfflinePlayer(args[5]);
            lawyer2Salary = Integer.parseInt(args[6]);
            judge1 = Bukkit.getOfflinePlayer(args[7]);
            judge2 = Bukkit.getOfflinePlayer(args[8]);
            judge3 = Bukkit.getOfflinePlayer(args[9]);
            judge4 = Bukkit.getOfflinePlayer(args[10]);
        }
        catch(Exception e)
        {
            sender.sendMessage("§cYou messed up.");
            return false;
        }

        long lawyerAvg = avg(lawyer1Salary, lawyer2Salary);
        
        pay(accused, -lawyer1Salary - lawyer2Salary - deposit);
        pay(prosecutor, deposit);
        pay(lawyer1, lawyer1Salary);
        pay(lawyer2, lawyer2Salary);
        pay(judge1, Math.round(lawyerAvg - 0.1 * lawyerAvg));
        pay(judge2, Math.round(lawyerAvg - 0.1 * lawyerAvg));
        pay(judge3, Math.round(lawyerAvg - 0.1 * lawyerAvg));
        pay(judge4, Math.round(lawyerAvg - 0.1 * lawyerAvg));

        for(Player i : Bukkit.getOnlinePlayers())
        {
            Main.updateScoreboard(i);
        }


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
        String timeStamp = dtf.format(LocalDateTime.now());
        String log = "";
        for(String i : logs)
        {
            log += "\t" + i + "\n";
        }

        Main.getPlugin().getLogger().info("\n[dish]\n" + log + "[dish]\n");
        Main.getPlugin().logToFile("dishLog.txt", "time: " + timeStamp + "\njudge: " + sender.getName() + "\n" + log);

        logs.clear();
        return false;
    }

    private void pay(OfflinePlayer player, long amount)
    {
        FileConfiguration config = Main.getPlugin().getConfig();

        config.set("balances." + player.getName(), config.getInt("balances." + player.getName()) + amount);
        Main.getPlugin().saveConfig();

        logs.add(player.getName() + ": + " + amount);
    }

    private long avg(int a, int b)
    {
        double res = (a + b) / 2;
        return Math.round(res);
    }
}
