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
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandRandomEnchant implements TabExecutor {

    private final RandomEnchant randomEnchant;
    private final BukkitAudiences audiences;

    public CommandRandomEnchant(RandomEnchant randomEnchant) {
        this.randomEnchant = randomEnchant;
        this.audiences = randomEnchant.getAdventure();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            audiences.sender(sender).sendMessage(Component.text("RandomEnchant version " + randomEnchant.getDescription().getVersion() + ". Created by hyperdefined.").color(NamedTextColor.GREEN));
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.isOp() || sender.hasPermission("randomenchant.reload")) {
                    randomEnchant.loadConfig();
                    audiences.sender(sender).sendMessage(Component.text("Configuration reloaded!").color(NamedTextColor.GREEN));
                } else {
                    audiences.sender(sender).sendMessage(Component.text("You do not have permission for this command.").color(NamedTextColor.RED));
                }
            } else {
                audiences.sender(sender).sendMessage(Component.text("Invalid sub-command.").color(NamedTextColor.RED));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Collections.singletonList("reload");
    }
}
