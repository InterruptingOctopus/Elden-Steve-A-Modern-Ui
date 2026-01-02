package com.interruptingoctopus.eldensteve_modernui.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * A component that renders the player's XP level inside a scaled-up selection sprite.
 */
public class XpOrb implements IRenderable {

    private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_selection");
    private final float scale;

    public XpOrb(float scale) {
        this.scale = scale;
    }

    @Override
    public void render(GuiGraphics guiGraphics, @Nullable Player player, int x, int y) {
        if (player == null) return;

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(this.scale, this.scale);

        // Draw the scaled-up selection sprite
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_SPRITE, 0, 0, 24, 24);

        // Draw the XP level number centered inside
        String xpText = String.valueOf(player.experienceLevel);
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(xpText);
        int textHeight = font.lineHeight;

        // Calculate position to center the text within the original 24x24 sprite area
        float textX = (24 - textWidth) / 2.0f;
        float textY = (24 - textHeight) / 2.0f;

        // The text needs to be scaled down to counteract the parent's scale up
        float textScale = 1.0f / this.scale;
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(textScale, textScale);
        // We also need to adjust the position to account for the down-scaling
        guiGraphics.drawString(font, xpText, (int)(textX * this.scale), (int)(textY * this.scale), 0x80FF20); // Green color
        guiGraphics.pose().popMatrix();

        guiGraphics.pose().popMatrix();
    }

    @Override
    public int getUnscaledWidth(@Nullable Player player) {
        return (int) (24 * this.scale);
    }

    @Override
    public int getUnscaledHeight() {
        return (int) (24 * this.scale);
    }
}
