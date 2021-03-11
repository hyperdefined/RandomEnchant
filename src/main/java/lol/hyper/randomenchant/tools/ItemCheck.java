package lol.hyper.randomenchant.tools;

import lol.hyper.randomenchant.RandomEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemCheck {

    private final RandomEnchant randomEnchant;

    public ItemCheck(RandomEnchant randomEnchant) {
        this.randomEnchant = randomEnchant;
    }

    // Hard code the different item types that we want to enchant
    public final String[] enchantableItems = { "pickaxe", "sword", "shovel", "axe", "hoe", "bow", "helmet", "chestplate", "leggings", "boots" };

    /**
     * Checks if we can enchant the tool/armor.
     * @param item The item to check.
     * @return If the item can be enchanted or not.
     */
    public boolean canWeEnchantThis(ItemStack item) {
        String itemName = item.getType().toString().toLowerCase();

        // Check if the item is a tool or armor piece
        // This is to filter out any item we don't want
        if (Arrays.stream(enchantableItems).noneMatch(itemName::contains)) {
            return false;
        }

        // Hard code bows since it doesn't follow the same name conventions as armor/tools
        String itemType;
        String itemMaterial = null;
        if (!itemName.equals("bow")) {
            itemType = itemName.substring(itemName.indexOf("_") + 1);
            itemMaterial = itemName.substring(0, itemName.indexOf("_"));
        } else {
            itemType = "bow";
        }

        // Check the config first to see if the item is disabled
        // Check the material type after to see if it's disabled
        switch(itemType) {
            case "pickaxe":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.tools.pickaxe")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
            case "sword":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.tools.sword")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
            case "shovel":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.tools.shovel")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
            case "axe":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.tools.axe")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
            case "hoe":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.tools.hoe")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
            case "bow":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.tools.bow")) {
                    return true;
                }
                break;
            case "helmet":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.armor.helmet")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
            case "chestplate":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.armor.chestplate")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
            case "leggings":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.armor.leggings")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
            case "boots":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.armor.boots")) {
                    return isMaterialExcluded(itemMaterial);
                }
                break;
        }
        return false;
    }

    /**
     * This will check to see if the item material is excluded.
     * @param material The item's material.
     * @return True if excluded, false if not.
     */
    private boolean isMaterialExcluded(String material) {
        return randomEnchant.config.getStringList("excluded-materials").contains(material);
    }


    // https://bukkit.org/threads/random-enchantment-on-an-item.280638/#post-2583381
    /**
     * Enchant an item randomly.
     * @param item The item to enchant.
     * @return The item with enchant(s) on them.
     */
    public ItemStack randomEnchantment(ItemStack item) {
        // Store all possible enchantments
        List<Enchantment> possible = new ArrayList<>();

        // Check if we are gonna use unsupported enchants
        // If not, then only add supported enchants for that item
        // If we are, then just add all of them
        if (!randomEnchant.config.getBoolean("enchant-items-with-unsupported-enchants")) {
            for (Enchantment ench : Enchantment.values()) {
                if (ench.canEnchantItem(item)) {
                    possible.add(ench);
                }
            }
        } else {
            possible.addAll(Arrays.asList(Enchantment.values()));
        }

        Random r = new Random();
        // Get our min and max levels
        int minLevel = randomEnchant.config.getInt("enchantment-level-range.min");
        int maxLevel = randomEnchant.config.getInt("enchantment-level-range.max");

        // Get how many enchants we want to add to the item
        int totalEnchants;
        if (randomEnchant.config.getBoolean("total-enchants-on-items.one-enchant")) {
            totalEnchants = 1;
        } else {
            int minEnchants = randomEnchant.config.getInt("total-enchants-on-items.min");
            int maxEnchants = randomEnchant.config.getInt("total-enchants-on-items.max");
            totalEnchants = r.nextInt((maxEnchants - minEnchants) + 1) + minEnchants;
        }

        // Add the enchants
        for (int i = 0; i < totalEnchants; i++) {
            // Shuffle the list of enchants for more rng
            Collections.shuffle(possible);
            // Get a random enchant from the list
            Enchantment chosen = possible.get((r.nextInt(possible.size())));
            // Check if we want to use default vanilla limits
            // If not, use the min and max from the config
            if (randomEnchant.config.getBoolean("enchantment-level-range.use-default-limits")) {
                item.addUnsafeEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
            } else {
                // Get a random level for the enchant
                int randomLevel = r.nextInt((maxLevel - minLevel) + 1) + minLevel;
                item.addUnsafeEnchantment(chosen, randomLevel);
            }
        }
        return item;
    }
}
