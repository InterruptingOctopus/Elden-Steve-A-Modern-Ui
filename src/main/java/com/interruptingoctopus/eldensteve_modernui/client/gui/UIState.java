package com.interruptingoctopus.eldensteve_modernui.client.gui;

/**
 * A central class to hold all global UI state. This is the single source of truth.
 */
public class UIState {

    /**
     * The currently selected slot index (0-4) for the main-hand hotbar.
     * This corresponds to inventory slots 4-8.
     */
    public static int mainHandIndex = 0;

    /**
     * The currently selected slot index (0-4) for the off-hand hotbar.
     * This corresponds to the off-hand slot (index 0) and inventory slots 0-3 (indices 1-4).
     */
    public static int offHandIndex = 0;
}
