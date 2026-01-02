package com.interruptingoctopus.eldensteve_modernui.client.gui;

import com.interruptingoctopus.eldensteve_modernui.EldenSteveModernUi;
import net.minecraft.resources.Identifier;

/**
 * Holds the Identifiers for the custom HUD layers.
 */
public class ModGuiLayers {
    // The single root for the entire custom HUD
    public static final Identifier MOD_UI_ROOT = Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "mod_ui_root");

    // Individual Bars (used for identification, not direct rendering)
    public static final Identifier HEALTH_BAR = Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "health_bar");
    public static final Identifier MANA_BAR = Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "mana_bar");
    public static final Identifier STAMINA_BAR = Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "stamina_bar");
    public static final Identifier XP_BAR = Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "xp_bar");
    public static final Identifier HUNGER_BAR = Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "hunger_bar");

    // Hotbar
    public static final Identifier CUSTOM_HOTBAR = Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "custom_hotbar");
}
