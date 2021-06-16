package lol.hyper.randomenchant.events;

import lol.hyper.randomenchant.RandomEnchant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

public class CraftEvent implements Listener {

    private final RandomEnchant randomEnchant;

    public CraftEvent(RandomEnchant randomEnchant) {
        this.randomEnchant = randomEnchant;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the event was cancelled so we don't break shit
        // Also check if the item is null
        if (event.isCancelled() || event.getCurrentItem() == null) {
            return;
        }

        // Only check on crafting table or smithing table
        if (event.getInventory() instanceof CraftingInventory || event.getInventory() instanceof SmithingInventory) {
            // Check if the item already has an enchantment.
            // If we don't check this, the player can just keep spam clicking the
            // result slot and enchant the same item constantly
            if (event.getCurrentItem().getEnchantments().size() > 0) {
                return;
            }

            // If the item was clicked on the result slot
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                ItemStack item = event.getCurrentItem();

                // Check if we want to enchant everything
                if (randomEnchant.config.getBoolean("enchant-everything")) {
                    ItemStack newItem = randomEnchant.itemCheck.randomEnchantment(item);
                    event.setCurrentItem(newItem);
                }

                // Check if the item can be enchanted
                if (randomEnchant.itemCheck.canWeEnchantThis(item)) {
                    ItemStack newItem = randomEnchant.itemCheck.randomEnchantment(item);
                    event.setCurrentItem(newItem);
                }
            }
        }
    }
}
