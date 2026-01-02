package com.interruptingoctopus.eldensteve_modernui.client.gui;

import com.interruptingoctopus.eldensteve_modernui.EldenSteveModernUi;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Defines and registers all custom key mappings for the mod.
 */
public class ModKeyMappings {

    public static final KeyMapping SWAP_HOTBAR_KEY = new KeyMapping(
        "key." + EldenSteveModernUi.MODID + ".swap_hotbar", // A unique translation key for the key's name
        GLFW.GLFW_KEY_R, // The default key is now 'R'
        new KeyMapping.Category(Identifier.fromNamespaceAndPath(EldenSteveModernUi.MODID, "key.categories.elden_steve_modern_ui")) // The category in the controls menu
    );

    /**
     * Registers the key mappings with the game.
     * This method is called by the event bus.
     * @param event The registration event.
     */
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(SWAP_HOTBAR_KEY);
    }
}
