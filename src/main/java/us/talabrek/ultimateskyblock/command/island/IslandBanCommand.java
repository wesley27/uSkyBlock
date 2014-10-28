package us.talabrek.ultimateskyblock.command.island;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.talabrek.ultimateskyblock.ICommand;
import us.talabrek.ultimateskyblock.UUIDPlayerInfo;
import us.talabrek.ultimateskyblock.uSkyBlock;

import java.util.ArrayList;
import java.util.List;

public class IslandBanCommand implements ICommand {

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getPermission() {
        return "usb.island.ban";
    }

    @Override
    public String getUsageString(String label, CommandSender sender) {
        return label + ChatColor.GREEN + " [<player>]";
    }

    @Override
    public String getDescription() {
        return "Bans/unbans <player> from your island.";
    }

    @Override
    public boolean canBeConsole() {
        return false;
    }

    @Override
    public boolean canBeCommandBlock() {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length > 1)
            return false;

        UUIDPlayerInfo info = uSkyBlock.getInstance().getPlayer(((Player) sender).getUniqueId());

        if (info == null) {
            sender.sendMessage(ChatColor.RED + "You have not started skyblock. Please use " + ChatColor.YELLOW + "/island" + ChatColor.RED + " to begin");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "The following players are banned from warping to your island:");
            sender.sendMessage(ChatColor.RED + info.getBanned().toString());
            sender.sendMessage(ChatColor.YELLOW + "To ban/unban from your island, use /island ban <player>");
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

            if (!player.hasPlayedBefore())
                player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Unknown player " + args[0]);
                return true;
            }

            if (info.isBanned(player.getUniqueId())) {
                info.removeBan(player.getUniqueId());
                sender.sendMessage(ChatColor.YELLOW + "You have unbanned " + ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " from warping to your island.");
            } else {
                info.addBan(player.getUniqueId());
                sender.sendMessage(ChatColor.YELLOW + "You have banned " + ChatColor.RED + player.getName() + ChatColor.YELLOW + " from warping to your island.");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            ArrayList<String> players = new ArrayList<String>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    if (sender instanceof Player && ((Player) sender).canSee(player))
                        players.add(player.getName());
                }
            }

            return players;
        }

        return null;
    }

}
