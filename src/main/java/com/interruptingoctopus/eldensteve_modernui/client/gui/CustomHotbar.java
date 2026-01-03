package com.interruptingoctopus.eldensteve_modernui.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Renders one side of a custom hotbar. This is a "dumb" component.
 */
public class CustomHotbar implements IRenderable {

    private static final Identifier HOTBAR_TEXTURE = Identifier.withDefaultNamespace("textures/gui/sprites/hud/hotbar.png");
    private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_selection");
    private static final int HOTBAR_TEXTURE_WIDTH = 182;
    // The actual texture is 22 pixels high. We define this constant to match the texture size
    // to avoid stretching when we render a taller area (extending UVs).
    private static final int HOTBAR_TEXTURE_HEIGHT = 22;

    private final MeterBar.Alignment alignment;
    private final int numSlots = 5;

    public CustomHotbar(MeterBar.Alignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public void render(GuiGraphics guiGraphics, @Nullable Player player, int x, int y) {
        // This method is no longer used directly. See the overloaded version.
    }

    /**
     * Renders the hotbar at the given coordinates, highlighting the specified slot.
     * @param selectedIndex The local index (0-4) of the slot to highlight.
     */
    public void render(GuiGraphics guiGraphics, @Nullable Player player, int x, int y, int selectedIndex) {
        if (player == null) return;

        int sideWidth = 1 + (this.numSlots * 20);
        int uOffset = (this.alignment == MeterBar.Alignment.LEFT) ? 0 : HOTBAR_TEXTURE_WIDTH - sideWidth;

        // --- Render Background ---
        // We render the texture at its native height (22) to avoid stretching or repeating.
        // The component itself reports a height of 25 to reserve space in the layout.
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED, HOTBAR_TEXTURE,
            x, y,
            uOffset, 0,
            sideWidth, 22,
            sideWidth, 22,
            HOTBAR_TEXTURE_WIDTH, HOTBAR_TEXTURE_HEIGHT,
            -1
        );

        // --- Render Items and Selector ---
        for (int i = 0; i < 5; i++) {
            int slotX = x + 1 + (i * 20);
            renderSlot(guiGraphics, (LocalPlayer) player, slotX, y, i, selectedIndex, (i == selectedIndex));
        }
    }

    private void renderSlot(GuiGraphics guiGraphics, LocalPlayer player, int x, int y, int visualIndex, int selectedIndex, boolean isSelected) {
        ItemStack itemStack;
        
        if (this.alignment == MeterBar.Alignment.LEFT) {
            // Left Bar (Off-hand)
            // We use dynamic mapping to ensure the selected item is visually stable
            // but physically corresponds to the offhand slot (40).
            int k = selectedIndex;
            
            if (visualIndex == k) {
                itemStack = player.getInventory().getItem(40); // Active off-hand
            } else if (visualIndex == 0) {
                itemStack = getStackInBaseSlot(player, k);
            } else {
                itemStack = getStackInBaseSlot(player, visualIndex);
            }
        } else {
            // Right Bar (Main-hand)
            itemStack = player.getInventory().getItem(visualIndex + 4); // Slots 4-8
        }

        if (!itemStack.isEmpty()) {
            int itemX = x + 2;
            int itemY = y + 3;
            guiGraphics.renderItem(player, itemStack, itemX, itemY, 0);
            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemStack, itemX, itemY);
        }

        if (isSelected) {
            // Extended height to 25
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_SPRITE, x - 2, y - 1, 24, 25);
        }
    }

    private ItemStack getStackInBaseSlot(Player player, int index) {
        if (index == 0) return player.getInventory().getItem(40);
        return player.getInventory().getItem(index - 1); // Slots 0-3
    }

    @Override
    public int getUnscaledWidth(@Nullable Player player) {
        return 1 + (this.numSlots * 20);
    }

    @Override
    public int getUnscaledHeight() {
        return 25;
    }
}
