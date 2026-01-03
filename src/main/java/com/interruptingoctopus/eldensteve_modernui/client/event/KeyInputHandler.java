package com.interruptingoctopus.eldensteve_modernui.client.event;

import com.interruptingoctopus.eldensteve_modernui.client.gui.ModKeyMappings;
import com.interruptingoctopus.eldensteve_modernui.client.gui.UIState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.ClickType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Handles all custom input events for the mod.
 */
public class KeyInputHandler {

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
            int oldIndex = UIState.offHandIndex;
            int newIndex = (oldIndex + scrollDirection + 5) % 5;
            
            if (oldIndex != newIndex) {
                UIState.offHandIndex = newIndex;
                performOffhandSwap(oldIndex, newIndex);
            }
        } else {
            // Default: Scroll the main-hand selection index
            UIState.mainHandIndex = (UIState.mainHandIndex + scrollDirection + 5) % 5;
            // Sync the vanilla selected slot to our main-hand index
            mc.player.getInventory().setSelectedSlot(UIState.mainHandIndex + 4);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        // Prioritize the off-hand item usage over the main-hand item usage.
        // This ensures that right-clicking activates the item in the left hotbar (off-hand).
        if (event.getHand() == InteractionHand.MAIN_HAND) {
            if (!event.getEntity().getOffhandItem().isEmpty()) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.PASS);
            }
        }
    }

    private static void performOffhandSwap(int oldIndex, int newIndex) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameMode == null) return;

        // 1. Restore old item to its slot (if it wasn't the base offhand slot)
        if (oldIndex != 0) {
            int slot = getContainerSlot(oldIndex);
            swapContainerSlots(mc, 45, slot); // 45 is Offhand in Container 0
        }

        // 2. Bring new item from its slot (if it isn't the base offhand slot)
        if (newIndex != 0) {
            int slot = getContainerSlot(newIndex);
            swapContainerSlots(mc, 45, slot);
        }
    }

    private static int getContainerSlot(int index) {
        // index 1 -> Inv 0 -> Cont 36
        // index 2 -> Inv 1 -> Cont 37
        // ...
        return 36 + (index - 1);
    }

    private static void swapContainerSlots(Minecraft mc, int slotA, int slotB) {
        // Click A (Pickup)
        mc.gameMode.handleInventoryMouseClick(0, slotA, 0, ClickType.PICKUP, mc.player);
        // Click B (Swap)
        mc.gameMode.handleInventoryMouseClick(0, slotB, 0, ClickType.PICKUP, mc.player);
        // Click A (Place)
        mc.gameMode.handleInventoryMouseClick(0, slotA, 0, ClickType.PICKUP, mc.player);
    }
}
