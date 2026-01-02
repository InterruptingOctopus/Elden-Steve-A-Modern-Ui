package com.interruptingoctopus.eldensteve_modernui.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * An interface for any UI component that can be rendered on the screen.
 */
public interface IRenderable {

    /**
     * Renders the component at the given absolute screen coordinates.
     * The component is responsible for its own PoseStack transformations.
     *
     * @param guiGraphics The graphics context.
     * @param player The player entity.
     * @param x The absolute x-coordinate to render at.
     * @param y The absolute y-coordinate to render at.
     */
    void render(GuiGraphics guiGraphics, @Nullable Player player, int x, int y);

    /**
     * Gets the unscaled width of the component.
     *
     * @param player The player entity.
     * @return The width in unscaled pixels.
     */
    int getUnscaledWidth(@Nullable Player player);

    /**
     * Gets the unscaled height of the component.
     *
     * @return The height in unscaled pixels.
     */
    int getUnscaledHeight();
}
