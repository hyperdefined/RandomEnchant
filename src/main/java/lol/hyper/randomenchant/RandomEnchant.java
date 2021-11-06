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

package lol.hyper.randomenchant;

import lol.hyper.randomenchant.commands.CommandRandomEnchant;
import lol.hyper.randomenchant.events.CraftEvent;
import lol.hyper.randomenchant.tools.ItemCheck;
import lol.hyper.randomenchant.tools.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class RandomEnchant extends JavaPlugin {

    public final File configFile = new File(this.getDataFolder(), "config.yml");
    public final Logger logger = this.getLogger();
    public FileConfiguration config;
    public ItemCheck itemCheck;
    public CraftEvent craftEvent;
    public CommandRandomEnchant commandRandomEnchant;

    @Override
    public void onEnable() {
        itemCheck = new ItemCheck(this);
        craftEvent = new CraftEvent(this);
        commandRandomEnchant = new CommandRandomEnchant(this);
        loadConfig();
        this.getCommand("randomenchant").setExecutor(commandRandomEnchant);

        Bukkit.getServer().getPluginManager().registerEvents(craftEvent, this);

        new Updater(this, 89994).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("You are running the latest version.");
            } else {
                logger.info(
                        "There is a new version available! Please download at https://www.spigotmc.org/resources/randomenchant.89994/");
            }
        });
        Metrics metrics = new Metrics(this, 10627);
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            this.saveResource("config.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        int CONFIG_VERSION = 4;
        if (config.getInt("config-version") != CONFIG_VERSION) {
            logger.warning("You configuration is out of date! Some features may not work!");
        }

        itemCheck.blackListedMaterials.put("wooden", config.getBoolean("enabled-materials.wood"));
        itemCheck.blackListedMaterials.put("stone", config.getBoolean("enabled-materials.stone"));
        itemCheck.blackListedMaterials.put("iron", config.getBoolean("enabled-materials.iron"));
        itemCheck.blackListedMaterials.put("diamond", config.getBoolean("enabled-materials.diamond"));
        itemCheck.blackListedMaterials.put("golden", config.getBoolean("enabled-materials.gold"));
        itemCheck.blackListedMaterials.put("netherite", config.getBoolean("enabled-materials.netherite"));
    }
}
