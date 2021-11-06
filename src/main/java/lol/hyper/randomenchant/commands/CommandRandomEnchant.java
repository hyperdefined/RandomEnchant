/*
 * This file is part of RandomEnchant.
 *
 * RandomEnchant is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RandomEnchant is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RandomEnchant.  If not, see <https://www.gnu.org/licenses/>.
 */

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
            sender.sendMessage(ChatColor.GREEN + "RandomEnchant version "
                    + randomEnchant.getDescription().getVersion() + ". Created by hyperdefined.");
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
