package me.lily.bllry.modules.impl.visuals;

import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.settings.impl.ColorSetting;
import me.lily.bllry.settings.impl.NumberSetting;
import me.lily.bllry.utils.color.ColorUtils;

@RegisterModule(name = "Atmosphere", description = "Modifies the world's atmosphere, such as time and color.", category = Module.Category.VISUALS)
public class AtmosphereModule extends Module {
    public BooleanSetting modifyTime = new BooleanSetting("ModifyTime", "Modifies the world's time.", true);
    public NumberSetting time = new NumberSetting("Time", "The time that the world will be set to.", new BooleanSetting.Visibility(modifyTime, true), 200, -200, 200);
    public BooleanSetting modifyFog = new BooleanSetting("ModifyFog", "Modifies certain things about the world's fog.", false);
    public NumberSetting fogStart = new NumberSetting("FogStart", "The start value of the world's fog.", new BooleanSetting.Visibility(modifyFog, true), 50, 0, 300);
    public NumberSetting fogEnd = new NumberSetting("FogEnd", "The end value of the world's fog.", new BooleanSetting.Visibility(modifyFog, true), 150, 0, 300);
    public ColorSetting fogColor = new ColorSetting("FogColor", "Modifies the color of the world's fog.", new BooleanSetting.Visibility(modifyFog, true), ColorUtils.getDefaultOutlineColor());
}
