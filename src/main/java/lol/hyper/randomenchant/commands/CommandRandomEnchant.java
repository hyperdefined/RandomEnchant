package lol.hyper.randomenchant.commands;

import lol.hyper.randomenchant.RandomEnchant;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

public class CommandRandomEnchant implements TabExecutor {

    private final RandomEnchant randomEnchant;

    public CommandRandomEnchant(RandomEnchant randomEnchant) {
        this.randomEnchant = randomEnchant;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "RandomEnchant version " + randomEnchant.getDescription().getVersion() + ". Created by hyperdefined.");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.isOp() || sender.hasPermission("randomenchant.reload")) {
                    randomEnchant.loadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid sub-command.");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.singletonList("reload");
    }
}