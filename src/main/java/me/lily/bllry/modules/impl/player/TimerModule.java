package me.lily.bllry.modules.impl.player;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.TickEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.NumberSetting;

@RegisterModule(name = "Timer", description = "Makes your game run at a faster tick speed.", category = Module.Category.PLAYER)
public class TimerModule extends Module {
    public NumberSetting multiplier = new NumberSetting("Multiplier", "The multiplier that will be added to the game's speed.", 1.0f, 0.0f, 20.0f);

    @SubscribeEvent(priority = Integer.MIN_VALUE)
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        Bllry.WORLD_MANAGER.setTimerMultiplier(multiplier.getValue().floatValue());
    }

    @Override
    public void onEnable() {
        Bllry.WORLD_MANAGER.setTimerMultiplier(multiplier.getValue().floatValue());
    }

    @Override
    public void onDisable() {
        Bllry.WORLD_MANAGER.setTimerMultiplier(1.0f);
    }

    @Override
    public String getMetaData() {
        return String.valueOf(multiplier.getValue().floatValue());
    }
}
