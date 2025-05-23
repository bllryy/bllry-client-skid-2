package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;
import net.minecraft.util.math.BlockPos;

@Getter @AllArgsConstructor
public class BreakBlockEvent extends Event {
    private final BlockPos pos;
}
