package me.lily.bllry.modules.impl.visuals;

import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.NumberSetting;

@RegisterModule(name = "AspectRatio", description = "Modifies the game's aspect ratio.", category = Module.Category.VISUALS)
public class AspectRatioModule extends Module {
    public NumberSetting ratio = new NumberSetting("Ratio", "The aspect ratio that will be applied to the game's rendering.", 1.78f, 0.0f, 5.0f);

    @Override
    public String getMetaData() {
        return String.valueOf(ratio.getValue().floatValue());
    }
}
