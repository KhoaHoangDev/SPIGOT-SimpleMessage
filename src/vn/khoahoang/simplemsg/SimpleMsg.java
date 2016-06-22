package vn.khoahoang.simplemsg;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import java.io.File;

/**
 * Created by khoan on 6/20/2016.
 */
public class SimpleMsg extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();
    public static File cfg;

    @Override
    public void onEnable() {
        this.getLogger().info(ChatColor.RED + "Start loading... :D");
        Bukkit.getPluginManager().registerEvents(this, this);
        cfg = new File(this.getDataFolder(), "config.yml");
        this.config.addDefault("join_msg", "&eWelcome to our server, &c%player_name% &e:D");
        this.config.addDefault("death_msg", "&d%player_name% &chas been slain.");
        this.config.addDefault("protectmode_enabled", false);
        this.config.addDefault("nogrief_msg", "&cHey &2%player_name%&c, you cannot break the blocks!");
        this.config.options().copyDefaults(true);
        this.saveConfig();
        this.getCommand("sannounce").setExecutor(this);
        this.getLogger().info(ChatColor.GREEN + "COMPLETED! Plugin SimpleMessage - Created by KhoaHoangVN");
        if (!config.getBoolean("protectmode_enabled")) {
            this.getLogger().info(ChatColor.AQUA + "Protect Mode turned " + ChatColor.RED + "off!");
            this.getLogger().info(ChatColor.GREEN + "You can edit the Protect Mode in the config!");
        } else if (config.getBoolean("protectmode_enabled")) {
            this.getLogger().info(ChatColor.AQUA + "Protect Mode turned " + ChatColor.GREEN + "on!");
            this.getLogger().info(ChatColor.GREEN + "You can edit the Protect Mode in the config!");
        } else {
            config.set("protectmode_enabled", false);
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info(ChatColor.RED + "Plugin disabled! Thanks for using the plugin! :D");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String r1 = config.getString("join_msg");
        r1 = r1.replaceAll("%player_name%", player.getName());
        if (player instanceof Player) {
            player.sendMessage(PlaceholderAPI.setPlaceholders(player, r1));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String r2 = config.getString("death_msg");
        r2 = r2.replaceAll("%player_name%", player.getName());
        if (player instanceof Player) {
            Bukkit.broadcastMessage(PlaceholderAPI.setPlaceholders(player, r2));
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String r3 = config.getString("nogrief_msg");
        r3 = r3.replaceAll("%player_name%", player.getName());
        if (config.getBoolean("protectmode_enabled")) {
            if (!player.isOp() || !player.hasPermission("simplemsg.protectmode.allowed")) {
                player.sendMessage(PlaceholderAPI.setPlaceholders(player, r3));
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(false);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sannounce")) {
            if (sender.isOp()) {
                if (args.length >= 1) {
                    String msg = "";
                    for (int i = 0; args.length != i; i++) {
                        msg = msg + args[i] + " ";
                    }
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[ANNOUNCEMENT] " + sender.getName() + ChatColor.GRAY + " > " + ChatColor.LIGHT_PURPLE + msg);
                } else {
                    sender.sendMessage(ChatColor.RED + "Please type the content you want to announce!" + ChatColor.LIGHT_PURPLE + " /sa <msg>");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You cannot perform this action.");
            }
            return true;
        }
        return false;
    }
}
