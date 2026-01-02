package com.interruptingoctopus.eldensteve_modernui.client.gui;

import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

/**
 * Holds the functions required to get the current and maximum values for a specific meter bar.
 *
 * @param currentValue A function that takes a Player and returns the current value for the bar.
 * @param maxValue     A function that takes a Player and returns the maximum possible value for the bar.
 */
public record BarInfo(Function<Player, Float> currentValue, Function<Player, Float> maxValue) {
}
