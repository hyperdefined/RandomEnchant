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

package lol.hyper.randomenchant.events;

import lol.hyper.randomenchant.RandomEnchant;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

public class CraftEvent implements Listener {

    private final RandomEnchant randomEnchant;

    public CraftEvent(RandomEnchant randomEnchant) {
        this.randomEnchant = randomEnchant;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the event was cancelled, so we don't break shit
        // Also check if the item is null
        if (event.isCancelled() || event.getCurrentItem() == null) {
            return;
        }

        // Only check crafting/smithing inventories
        Inventory clicked = event.getInventory();
        if (clicked instanceof CraftingInventory || clicked instanceof SmithingInventory) {
            // Check if the item already has an enchantment.
            // If we don't check this, the player can just keep spam clicking the
            // result slot and enchant the same item constantly
            if (event.getCurrentItem().getEnchantments().size() > 0) {
                return;
            }

            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                ItemStack item = event.getCurrentItem();

                // Check if we want to enchant everything
                if (randomEnchant.config.getBoolean("enchant-everything")) {
                    ItemStack newItem = randomEnchant.itemCheck.randomEnchantment(item);
                    if (newItem == null) {
                        randomEnchant.logger.warning("Item returned null!");
                        return;
                    }
                    event.setCurrentItem(newItem);
                    event.setResult(Event.Result.ALLOW);
                }

                // Check if the item can be enchanted
                if (randomEnchant.itemCheck.canWeEnchantThis(item)) {
                    ItemStack newItem = randomEnchant.itemCheck.randomEnchantment(item);
                    if (newItem == null) {
                        randomEnchant.logger.warning("Item returned null!");
                        return;
                    }
                    event.setCurrentItem(newItem);
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
    }
}
