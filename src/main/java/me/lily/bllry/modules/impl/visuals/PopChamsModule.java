package me.lily.bllry.modules.impl.visuals;

import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.PlayerPopEvent;
import me.lily.bllry.events.impl.RenderWorldEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.ColorSetting;
import me.lily.bllry.settings.impl.ModeSetting;
import me.lily.bllry.settings.impl.NumberSetting;
import me.lily.bllry.utils.animations.Easing;
import me.lily.bllry.utils.color.ColorUtils;
import me.lily.bllry.utils.graphics.Renderer3D;
import me.lily.bllry.utils.minecraft.StaticPlayerEntity;

import java.awt.*;

@RegisterModule(name = "PopChams", description = "Renders chams when an entity pops a totem.", category = Module.Category.VISUALS)
public class PopChamsModule extends Module {
    public NumberSetting duration = new NumberSetting("Duration", "The duration for the pop chams fade.", 1500, 0, 5000);
    public ModeSetting mode = new ModeSetting("Mode", "The rendering that will be applied to the pop chams.", "Both", new String[]{"Fill", "Outline", "Both"});
    public ColorSetting fillColor = new ColorSetting("FillColor", "The color used for the fill rendering.", new ModeSetting.Visibility(mode, "Fill", "Both"), ColorUtils.getDefaultFillColor());
    public ColorSetting outlineColor = new ColorSetting("OutlineColor", "The color used for the outline rendering.", new ModeSetting.Visibility(mode, "Outline", "Both"), ColorUtils.getDefaultOutlineColor());

    private StaticPlayerEntity model;
    private long startTime;

    @SubscribeEvent
    public void onPlayerPop(PlayerPopEvent event) {
        if(event.getPlayer() == mc.player) return;
        this.model = new StaticPlayerEntity(event.getPlayer());
        this.startTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if(getNull() || model == null || !Renderer3D.isFrustumVisible(model.getBoundingBox()) || System.currentTimeMillis() - startTime > duration.getValue().intValue()) return;

        float ease = 1.0f - Easing.toDelta(startTime, duration.getValue().intValue());
        Color fill = ColorUtils.getColor(fillColor.getColor(), (int)(fillColor.getColor().getAlpha() * ease));
        Color out = ColorUtils.getColor(outlineColor.getColor(), (int)(outlineColor.getColor().getAlpha() * ease));

        model.render(event, mode.getValue().equals("Fill") || mode.getValue().equals("Both"), fill, mode.getValue().equals("Outline") || mode.getValue().equals("Both"), out);
    }
}
