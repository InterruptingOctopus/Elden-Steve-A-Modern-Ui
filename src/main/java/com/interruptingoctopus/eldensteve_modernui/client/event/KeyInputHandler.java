package com.interruptingoctopus.eldensteve_modernui.client.event;

import com.interruptingoctopus.eldensteve_modernui.client.gui.ModKeyMappings;
import com.interruptingoctopus.eldensteve_modernui.client.gui.UIState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;

/**
 * Handles all custom input events for the mod.
 */
public class KeyInputHandler {

    @SubscribeEvent
    public static void onKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        // Handle the vanilla "Swap Item" action (default 'F')
        if (event.getKeyMapping() == Minecraft.getInstance().options.keySwapOffhand) {
            swapItems();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.getScrollDeltaY() == 0) {
            return;
        }

        event.setCanceled(true);
        int scrollDirection = event.getScrollDeltaY() > 0 ? -1 : 1;

        if (ModKeyMappings.SWAP_HOTBAR_KEY.isDown()) {
            // 'R' is held: Scroll the off-hand selection index
            UIState.offHandIndex = (UIState.offHandIndex + scrollDirection + 5) % 5;
        } else {
            // Default: Scroll the main-hand selection index
            UIState.mainHandIndex = (UIState.mainHandIndex + scrollDirection + 5) % 5;
            // Sync the vanilla selected slot to our main-hand index
            mc.player.getInventory().setSelectedSlot(UIState.mainHandIndex + 4);
        }
    }

    /**
     * Swaps the item under the main-hand selector with the item under the off-hand selector.
     */
    private static void swapItems() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Inventory inventory = mc.player.getInventory();
        
        // Get the inventory slot for the main-hand selection
        int mainHandSlot = UIState.mainHandIndex + 4;

        // Get the inventory slot for the off-hand selection
        int offHandSlot;
        if (UIState.offHandIndex == 0) {
            offHandSlot = 40; // The actual off-hand slot
        } else {
            offHandSlot = UIState.offHandIndex - 1; // Slots 0-3
        }

        ItemStack mainHandStack = inventory.getItem(mainHandSlot);
        ItemStack offHandStack = inventory.getItem(offHandSlot);

        inventory.setItem(mainHandSlot, offHandStack);
        inventory.setItem(offHandSlot, mainHandStack);
    }
}
