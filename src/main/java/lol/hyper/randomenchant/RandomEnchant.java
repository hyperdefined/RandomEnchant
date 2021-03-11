package lol.hyper.randomenchant;

import lol.hyper.randomenchant.commands.CommandRandomEnchant;
import lol.hyper.randomenchant.events.CraftEvent;
import lol.hyper.randomenchant.tools.ItemCheck;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class RandomEnchant extends JavaPlugin {

    public final File configFile = new File(this.getDataFolder(), "config.yml");
    public FileConfiguration config;
    public final Logger logger = this.getLogger();

    public ItemCheck toolCheck;
    public CraftEvent craftEvent;
    public CommandRandomEnchant commandRandomEnchant;

    @Override
    public void onEnable() {
        loadConfig();
        toolCheck = new ItemCheck(this);
        craftEvent = new CraftEvent(this);
        commandRandomEnchant = new CommandRandomEnchant(this);

        this.getCommand("randomenchant").setExecutor(commandRandomEnchant);

        Bukkit.getServer().getPluginManager().registerEvents(craftEvent, this);

        Metrics metrics = new Metrics(this, 10627);
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            this.saveResource("config.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        int CONFIG_VERSION = 1;
        if (config.getInt("config-version") != CONFIG_VERSION) {
            logger.warning("You configuration is out of date! Some features may not work!");
        }
    }
}
