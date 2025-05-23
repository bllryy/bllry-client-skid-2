package me.lily.bllry.modules.impl.core;

import me.lily.bllry.Bllry;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.settings.impl.ColorSetting;
import me.lily.bllry.settings.impl.NumberSetting;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

@RegisterModule(name = "ClickGui", description = "Allows you to change and interact with the client's modules and settings through a GUI.", category = Module.Category.CORE, drawn = false, bind = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class ClickGuiModule extends Module {
    public BooleanSetting sounds = new BooleanSetting("Sounds", "Plays Minecraft UI sounds when interacting with the client's GUI.", true);
    public BooleanSetting blur = new BooleanSetting("Blur", "Whether or not to blur the background behind the GUI.", true);
    public NumberSetting scrollSpeed = new NumberSetting("ScrollSpeed", "The speed at which the scrolling of the frames will be at.", 15, 1, 50);
    public ColorSetting color = new ColorSetting("Color", "The color that will be used in the GUI.", new ColorSetting.Color(new Color(130, 202, 255), true, false));

    @Override
    public void onEnable() {
        if (mc.player == null) {
            setToggled(false);
            return;
        }

        mc.setScreen(Bllry.CLICK_GUI);
    }

    @Override
    public void onDisable() {
        mc.setScreen(null);
    }

    public boolean isRainbow() {
        if(color.isSync()) return Bllry.MODULE_MANAGER.getModule(ColorModule.class).color.isRainbow();
        return color.isRainbow();
    }
}
