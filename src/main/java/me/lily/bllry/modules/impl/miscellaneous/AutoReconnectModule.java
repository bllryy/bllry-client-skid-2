package me.lily.bllry.modules.impl.miscellaneous;

import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.NumberSetting;

@RegisterModule(name = "AutoReconnect", description = "Automatically reconnects you to a server after a specified time period.", category = Module.Category.MISCELLANEOUS)
public class AutoReconnectModule extends Module {
    public NumberSetting delay = new NumberSetting("Delay", "The amount of seconds that have to pass before reconnecting.", 5, 0, 20);

    @Override
    public String getMetaData() {
        return String.valueOf(delay.getValue().intValue());
    }
}
