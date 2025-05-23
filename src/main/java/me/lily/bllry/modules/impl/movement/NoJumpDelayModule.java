package me.lily.bllry.modules.impl.movement;

import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.NumberSetting;

@RegisterModule(name = "NoJumpDelay", description = "Removes the delay that slows down your jumping.", category = Module.Category.MOVEMENT)
public class NoJumpDelayModule extends Module {
    public NumberSetting ticks = new NumberSetting("Ticks", "The amount of ticks that have to be waited for before jumping again.", 1, 0, 20);

    @Override
    public String getMetaData() {
        return String.valueOf(ticks.getValue());
    }
}
