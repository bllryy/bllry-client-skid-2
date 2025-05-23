package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;
import net.minecraft.client.util.math.MatrixStack;

@Getter @AllArgsConstructor
public class RenderWorldEvent extends Event {
    private final MatrixStack matrices;
    private final float tickDelta;

    @Getter @AllArgsConstructor
    public static class Post extends Event {
        private final MatrixStack matrices;
        private final float tickDelta;
    }
}
