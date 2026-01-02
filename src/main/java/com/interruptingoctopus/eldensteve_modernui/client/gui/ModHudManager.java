package com.interruptingoctopus.eldensteve_modernui.client.gui;

import com.interruptingoctopus.eldensteve_modernui.EldenSteveModernUi;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the rendering of the entire custom HUD.
 */
public class ModHudManager {

    public static final Identifier METER_MAP_TEXTURE = Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "textures/gui/meter_map.png");
    public static final int METER_MAP_TEXTURE_WIDTH = 256;
    public static final int METER_MAP_TEXTURE_HEIGHT = 256;

    private static final Map<Identifier, Boolean> vanillaLayerVisibility = new HashMap<>();

    // --- HUD Component Hierarchy ---
    private static final List<MeterBar> leftMeters = new ArrayList<>();
    private static final List<MeterBar> rightMeters = new ArrayList<>();
    private static final CustomHotbar leftHotbar = new CustomHotbar(MeterBar.Alignment.LEFT);
    private static final CustomHotbar rightHotbar = new CustomHotbar(MeterBar.Alignment.RIGHT);
    private static final XpOrb xpOrb = new XpOrb(1.5f);
    public static final MeterBar xpBar;

    public static void setVanillaLayerVisibility(Identifier layerName, boolean isVisible) {
        vanillaLayerVisibility.put(layerName, isVisible);
    }

    @SubscribeEvent
    public static void onRenderVanillaGuiLayer(RenderGuiLayerEvent.Pre event) {
        Boolean isVisible = vanillaLayerVisibility.get(event.getName());
        if (isVisible != null && !isVisible) {
            event.setCanceled(true);
        }
    }

    /**
     * The single entry point for rendering the entire custom HUD.
     * @param deltaTracker The delta tracker for interpolation, required by the event but unused here.
     */
    public static void renderHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        // --- Layout Calculations ---
        int orbWidth = xpOrb.getUnscaledWidth(player);
        int hotbarHeight = leftHotbar.getUnscaledHeight();
        int xpBarHeight = xpBar.getUnscaledHeight();
        int baseY = screenHeight - hotbarHeight - xpBarHeight - 2;

        // --- Centerpiece: XP Orb ---
        int orbX = (screenWidth - orbWidth) / 2;
        int orbY = baseY - (xpOrb.getUnscaledHeight() - hotbarHeight) / 2;
        xpOrb.render(guiGraphics, player, orbX, orbY);

        // --- Hotbars connected to the Orb ---
        int leftHotbarX = orbX - leftHotbar.getUnscaledWidth(player);
        leftHotbar.render(guiGraphics, player, leftHotbarX, baseY, UIState.offHandIndex);

        int rightHotbarX = orbX + orbWidth;
        rightHotbar.render(guiGraphics, player, rightHotbarX, baseY, UIState.mainHandIndex);

        // --- Meters (Above Hotbars) ---
        int currentY = baseY;
        for (MeterBar meter : leftMeters) {
            currentY -= meter.getUnscaledHeight();
            meter.render(guiGraphics, player, leftHotbarX, currentY);
        }

        currentY = baseY;
        for (MeterBar meter : rightMeters) {
            currentY -= meter.getUnscaledHeight();
            int meterX = rightHotbarX + rightHotbar.getUnscaledWidth(player);
            meter.render(guiGraphics, player, meterX, currentY);
        }

        // --- Custom XP Bar (Below Hotbars) ---
        int customXpBarWidth = xpBar.getUnscaledWidth(player);
        int customXpBarX = (screenWidth - customXpBarWidth) / 2;
        int customXpBarY = baseY + hotbarHeight + 2;
        xpBar.render(guiGraphics, player, customXpBarX, customXpBarY);
    }

    static {
        // --- Hide Vanilla Layers ---
        setVanillaLayerVisibility(VanillaGuiLayers.PLAYER_HEALTH, false);
        setVanillaLayerVisibility(VanillaGuiLayers.FOOD_LEVEL, false);
        setVanillaLayerVisibility(VanillaGuiLayers.EXPERIENCE_LEVEL, true);
        setVanillaLayerVisibility(VanillaGuiLayers.HOTBAR, false);
        setVanillaLayerVisibility(VanillaGuiLayers.CONTEXTUAL_INFO_BAR, false);
        setVanillaLayerVisibility(VanillaGuiLayers.CONTEXTUAL_INFO_BAR_BACKGROUND, false);

        // --- Assemble HUD Hierarchy ---
        MeterBar.UvRect capUv = new MeterBar.UvRect(0, 0, 3, 7);
        MeterBar.UvRect containerUv = new MeterBar.UvRect(3, 1, 10, 5);
        MeterBar.UvRect fillUv = new MeterBar.UvRect(3, 8, 10, 5);

        leftMeters.add(new MeterBar(ModGuiLayers.HEALTH_BAR, 0xFFFF0000, new BarInfo(Player::getHealth, Player::getMaxHealth), capUv, containerUv, fillUv, MeterBar.Alignment.LEFT));
        leftMeters.add(new MeterBar(ModGuiLayers.MANA_BAR, 0xFF0000FF, new BarInfo(p -> 20.0f, p -> 20.0f), capUv, containerUv, fillUv, MeterBar.Alignment.LEFT));
        leftMeters.add(new MeterBar(ModGuiLayers.STAMINA_BAR, 0xFF00FF00, new BarInfo(p -> (float) p.getFoodData().getFoodLevel(), p -> 20.0f), capUv, containerUv, fillUv, MeterBar.Alignment.LEFT));

        rightMeters.add(new MeterBar(ModGuiLayers.HUNGER_BAR, 0xFFFFA500, new BarInfo(p -> (float) p.getFoodData().getFoodLevel(), p -> 20.0f), capUv, containerUv, fillUv, MeterBar.Alignment.RIGHT));

        xpBar = new MeterBar(ModGuiLayers.XP_BAR, 0xFFFFFFFF, new BarInfo(p -> p.experienceProgress * 200, p -> 200.0f), capUv, containerUv, fillUv, MeterBar.Alignment.LEFT);
    }
}
