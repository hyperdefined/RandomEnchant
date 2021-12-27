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

import lol.hyper.githubreleaseapi.GitHubRelease;
import lol.hyper.githubreleaseapi.GitHubReleaseAPI;
import lol.hyper.randomenchant.commands.CommandRandomEnchant;
import lol.hyper.randomenchant.events.CraftEvent;
import lol.hyper.randomenchant.tools.ItemCheck;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
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

        new Metrics(this, 10627);

        Bukkit.getScheduler().runTaskAsynchronously(this, this::checkForUpdates);
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            this.saveResource("config.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        int CONFIG_VERSION = 6;
        if (config.getInt("config-version") != CONFIG_VERSION) {
            logger.warning("You configuration is out of date! Some features may not work!");
        }

        itemCheck.blackListedMaterials.put("wooden", config.getBoolean("enabled-materials.wood"));
        itemCheck.blackListedMaterials.put("stone", config.getBoolean("enabled-materials.stone"));
        itemCheck.blackListedMaterials.put("iron", config.getBoolean("enabled-materials.iron"));
        itemCheck.blackListedMaterials.put("diamond", config.getBoolean("enabled-materials.diamond"));
        itemCheck.blackListedMaterials.put("golden", config.getBoolean("enabled-materials.gold"));
        itemCheck.blackListedMaterials.put("netherite", config.getBoolean("enabled-materials.netherite"));

        for (String key : config.getStringList("enchantments")) {
            itemCheck.possibleEnchantsFromConfig.clear();
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
            itemCheck.possibleEnchantsFromConfig.add(enchantment);
        }
    }

    public void checkForUpdates() {
        GitHubReleaseAPI api;
        try {
            api = new GitHubReleaseAPI("RandomEnchant", "hyperdefined");
        } catch (IOException e) {
            logger.warning("Unable to check updates!");
            e.printStackTrace();
            return;
        }
        GitHubRelease current = api.getReleaseByTag(this.getDescription().getVersion());
        GitHubRelease latest = api.getLatestVersion();
        if (current == null) {
            logger.warning("You are running a version that does not exist on GitHub. If you are in a dev environment, you can ignore this. Otherwise, this is a bug!");
            return;
        }
        int buildsBehind = api.getBuildsBehind(current);
        if (buildsBehind == 0) {
            logger.info("You are running the latest version.");
        } else {
            logger.warning("A new version is available (" + latest.getTagVersion() + ")! You are running version " + current.getTagVersion() + ". You are " + buildsBehind + " version(s) behind.");
        }
    }
}
