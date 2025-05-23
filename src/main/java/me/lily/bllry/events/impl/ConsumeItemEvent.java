package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;
import net.minecraft.item.ItemStack;

@Getter @AllArgsConstructor
public class ConsumeItemEvent extends Event {
    private final ItemStack stack;
}
