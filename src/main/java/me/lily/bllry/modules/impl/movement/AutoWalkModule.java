package me.lily.bllry.modules.impl.movement;

import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.TickEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;

@RegisterModule(name = "AutoWalk", description = "Automatically walks at all times.", category = Module.Category.MOVEMENT)
public class AutoWalkModule extends Module {
    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        mc.options.forwardKey.setPressed(true);
    }

    @Override
    public void onDisable() {
        if (mc.player == null || mc.world == null) return;
        mc.options.forwardKey.setPressed(false);
    }
}
