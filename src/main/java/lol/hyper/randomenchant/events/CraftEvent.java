package lol.hyper.randomenchant.events;

import lol.hyper.randomenchant.RandomEnchant;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class CraftEvent implements Listener {

    private final RandomEnchant randomEnchant;

    public CraftEvent(RandomEnchant randomEnchant) {
        this.randomEnchant = randomEnchant;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getSlotType() == InventoryType.SlotType.RESULT) {
            ItemStack item = event.getCurrentItem();

            // Ignore if there is not item clicked
            if (item.getType() == Material.AIR) {
                return;
            }
            // Check if the item can be enchanted
            // If it can, enchant it and change the item that was returned
            System.out.println(randomEnchant.toolCheck.canWeEnchantThis(item));
            if (randomEnchant.toolCheck.canWeEnchantThis(item)) {
                ItemStack newItem = randomEnchant.toolCheck.randomEnchantment(item);
                event.setCurrentItem(newItem);
                System.out.println(randomEnchant.toolCheck.canWeEnchantThis(item));
            }
        }
    }
}
