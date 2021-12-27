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

package lol.hyper.randomenchant.tools;

import lol.hyper.randomenchant.RandomEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemCheck {

    // Hard code the different item types that we want to enchant
    public final String[] enchantableItems = {
            "pickaxe", "sword", "shovel", "axe", "hoe", "bow", "helmet", "chestplate", "leggings", "boots", "fishing"
    };
    // Store which materials are blacklisted
    public final HashMap<String, Boolean> blackListedMaterials = new HashMap<>();
    private final RandomEnchant randomEnchant;
    public List<Enchantment> possibleEnchantsFromConfig = new ArrayList<>();

    public ItemCheck(RandomEnchant randomEnchant) {
        this.randomEnchant = randomEnchant;
    }

    /**
     * Checks if we can enchant the tool/armor.
     *
     * @param item The item to check.
     * @return If the item can be enchanted or not.
     */
    public boolean canWeEnchantThis(ItemStack item) {
        // Skip all checks if we want to enchant all items crafted
        if (randomEnchant.config.getBoolean("enchant-everything")) {
            return true;
        }

        String itemName = item.getType().toString().toLowerCase();

        // Check if the item is a tool or armor piece
        // This is to filter out any item we don't want
        if (Arrays.stream(enchantableItems).noneMatch(itemName::contains)) {
            return false;
        }

        // Hard code bows and fishing rods since they don't follow the same name conventions as armor/tools
        String itemType = null;
        String itemMaterial = null;

        if (itemName.contains("bow") || itemName.contains("fishing")) {
            if (itemName.contains("bow")) {
                itemType = "bow";
            }
            if (itemName.contains("fishing")) {
                itemType = "fishingrod";
            }
        } else {
            itemType = itemName.substring(itemName.indexOf("_") + 1);
            itemMaterial = itemName.substring(0, itemName.indexOf("_"));
        }

        if (itemType == null) {
            return false;
        }


        // Check the config first to see if the item is disabled
        // Check the material type after to see if it's disabled
        switch (itemType) {
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
            case "fishingrod":
                if (randomEnchant.config.getBoolean("items-to-be-enchanted.tools.fishingrod")) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * This will check to see if the item material is excluded.
     *
     * @param material The item's material.
     * @return True if excluded, false if not.
     */
    private boolean isMaterialExcluded(String material) {
        return blackListedMaterials.get(material);
    }

    // https://bukkit.org/threads/random-enchantment-on-an-item.280638/#post-2583381

    /**
     * Enchant an item randomly.
     *
     * @param item The item to enchant.
     * @return The item with enchant(s) on them.
     */
    public ItemStack randomEnchantment(ItemStack item) {
        // Store all possible enchantments
        List<Enchantment> possible = new ArrayList<>();

        // See if we want to enchant all items
        boolean enchantEverything = randomEnchant.config.getBoolean("enchant-everything");

        // Check if we are going to use unsupported enchants
        // If not, then only add supported enchants for that item
        // If we are, then just add all of them
        // We use "enchantEverything" here because regular items do not have "valid" enchants, so we skip this check and add all
        if (!enchantEverything || !randomEnchant.config.getBoolean("enchant-items-with-unsupported-enchants")) {
            for (Enchantment ench : possibleEnchantsFromConfig) {
                if (ench.canEnchantItem(item)) {
                    possible.add(ench);
                }
            }
        } else {
            possible.addAll(possibleEnchantsFromConfig);
        }

        Random r = new Random();
        // Get our min and max levels
        int minLevel = randomEnchant.config.getInt("enchantment-level-range.min");
        int maxLevel = randomEnchant.config.getInt("enchantment-level-range.max");

        // Get how many enchants we want to add to the item
        int minEnchants = randomEnchant.config.getInt("total-enchants-on-items.min");
        int maxEnchants = randomEnchant.config.getInt("total-enchants-on-items.max");
        int totalEnchants = r.nextInt((maxEnchants - minEnchants) + 1) + minEnchants;

        if (possibleEnchantsFromConfig.isEmpty()) {
            randomEnchant.logger.warning("There are no enchantments on the list! Not enchanting item!");
            return null;
        }

        if (possible.isEmpty()) {
            randomEnchant.logger.warning("There are no possible enchantments for item " + item.getType() + " that are present in your config.");
            return null;
        }

        // Add the enchants
        for (int i = 0; i < totalEnchants; i++) {
            // Shuffle the list of enchants for more rng
            Collections.shuffle(possible);
            // Get a random enchant from the list
            Enchantment chosen = possible.get((r.nextInt(possible.size())));
            // If the enchantment is not on the list, re-roll it
            while (!possibleEnchantsFromConfig.contains(chosen)) {
                chosen = possible.get((r.nextInt(possible.size())));
            }
            // Check if we want to use default vanilla limits
            // If not, use the min and max from the config
            if (randomEnchant.config.getBoolean("enchantment-level-range.use-default-limits")) {
                item.addUnsafeEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
            } else {
                // Get a random level for the enchantment
                int randomLevel = r.nextInt((maxLevel - minLevel) + 1) + minLevel;
                item.addUnsafeEnchantment(chosen, randomLevel);
            }
        }
        return item;
    }
}
