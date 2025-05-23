package me.lily.bllry.modules.impl.miscellaneous;

import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.StringSetting;

@RegisterModule(name = "NameProtect", description = "Hides your current in game name.", category = Module.Category.MISCELLANEOUS)
public class NameProtectModule extends Module {
    public StringSetting name = new StringSetting("Name", "The name to use as a replacement.", "Bllry");
}
