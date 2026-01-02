package com.interruptingoctopus.eldensteve_modernui.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * A component that renders a meter bar at a given screen coordinate.
 */
public class MeterBar implements IRenderable {

    private record ComponentConfig(Identifier texture, int u, int v, int width, int height, int tintColor) {}

    public enum Alignment {
        LEFT,
        RIGHT
    }

    private final BarInfo barInfo;
    private final ComponentConfig leftCap;
    private final ComponentConfig rightCap;
    private final ComponentConfig container;
    private final ComponentConfig fill;
    private final Alignment alignment;

    public record UvRect(int u, int v, int width, int height) {}

    public MeterBar(Identifier id, int fillTintColor, BarInfo barInfo, UvRect capUv, UvRect containerUv, UvRect fillUv, Alignment alignment) {
        this.barInfo = barInfo;
        this.alignment = alignment;

        this.leftCap = new ComponentConfig(ModHudManager.METER_MAP_TEXTURE, capUv.u, capUv.v, capUv.width, capUv.height, -1);
        this.rightCap = new ComponentConfig(ModHudManager.METER_MAP_TEXTURE, containerUv.u + containerUv.width, capUv.v, capUv.width, capUv.height, -1);
        this.container = new ComponentConfig(ModHudManager.METER_MAP_TEXTURE, containerUv.u, containerUv.v, containerUv.width, containerUv.height, -1);
        this.fill = new ComponentConfig(ModHudManager.METER_MAP_TEXTURE, fillUv.u, fillUv.v, fillUv.width, fillUv.height, fillTintColor);
    }

    @Override
    public int getUnscaledWidth(@Nullable Player player) {
        if (player == null) return 0;
        float maxValue = this.barInfo.maxValue().apply(player);
        if (maxValue <= 0) maxValue = 1;
        int containerUnscaledWidth = (int) Math.ceil(maxValue);
        return this.leftCap.width() + containerUnscaledWidth + this.rightCap.width();
    }

    @Override
    public int getUnscaledHeight() {
        return 7; // The height of the caps, which is the tallest part
    }

    @Override
    public void render(GuiGraphics guiGraphics, @Nullable Player player, int x, int y) {
        if (player == null) return;

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);

        if (this.alignment == Alignment.RIGHT) {
            guiGraphics.pose().translate(getUnscaledWidth(player), 0);
            guiGraphics.pose().scale(-1, 1);
        }

        float currentValue = this.barInfo.currentValue().apply(player);
        float maxValue = this.barInfo.maxValue().apply(player);
        if (maxValue <= 0) {
            currentValue = 0;
            maxValue = 1;
        }
        int containerUnscaledWidth = (int) Math.ceil(maxValue);
        int fillUnscaledWidth = (int) ((currentValue / maxValue) * containerUnscaledWidth);
        int leftCapUnscaledWidth = this.leftCap.width();

        int containerX = leftCapUnscaledWidth;
        int containerY = 1;

        renderComponent(guiGraphics, this.leftCap, 0, 0, leftCapUnscaledWidth);
        renderComponent(guiGraphics, this.container, containerX, containerY, containerUnscaledWidth);
        renderComponent(guiGraphics, this.fill, containerX, containerY, fillUnscaledWidth);
        int rightCapX = leftCapUnscaledWidth + containerUnscaledWidth;
        renderComponent(guiGraphics, this.rightCap, rightCapX, 0, this.rightCap.width());

        guiGraphics.pose().popMatrix();
    }

    private void renderComponent(GuiGraphics guiGraphics, ComponentConfig config, int x, int y, int unscaledWidth) {
        if (config.texture() == null || unscaledWidth <= 0) return;

        for (int i = 0; i < unscaledWidth; i += config.width()) {
            int segmentWidth = Math.min(config.width(), unscaledWidth - i);
            guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED, config.texture(),
                x + i, y,
                (float) config.u(), (float) config.v(),
                segmentWidth, config.height(),
                segmentWidth, config.height(),
                ModHudManager.METER_MAP_TEXTURE_WIDTH, ModHudManager.METER_MAP_TEXTURE_HEIGHT,
                config.tintColor()
            );
        }
    }
}
