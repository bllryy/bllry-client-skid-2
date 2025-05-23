package me.lily.bllry.modules.impl.movement;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.TickEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.NumberSetting;
import me.lily.bllry.utils.minecraft.EntityUtils;
import net.minecraft.util.Formatting;

@RegisterModule(name = "TickShift", description = "Manipulates minecraft timer to give you a small speed boost.", category = Module.Category.MOVEMENT)
public class TickShiftModule extends Module {
    public NumberSetting maxTicks = new NumberSetting("MaxTicks", "The maximum amount of ticks that the boost will be charging for.", 1, 1, 40);
    public NumberSetting delay = new NumberSetting("Delay", "The delay between each tick.", 4, 1, 10);
    public NumberSetting speed = new NumberSetting("Speed", "The speed of charging each tick.", 2.0f, 1.0f, 10.0f);

    int ticks = 0;
    int wait = 0;

    @Override
    public void onEnable() {
        if (getNull()) return;
        reset();
    }

    @Override
    public void onDisable() {
        if (getNull()) return;
        reset();
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (getNull()) return;

        if (mc.player.fallDistance >= 5.0f)
            return;

        if ((mc.player.sidewaysSpeed == 0.0f && mc.player.forwardSpeed == 0.0f && mc.player.fallDistance == 0.0f) || EntityUtils.getSpeed(mc.player, EntityUtils.SpeedUnit.KILOMETERS) <= 5) {
            Bllry.WORLD_MANAGER.setTimerMultiplier(1.0f);
            if(wait >= delay.getValue().intValue()) {
                if(ticks < maxTicks.getValue().intValue()) {
                    ticks++;
                }
                wait = 0;
            }
            wait++;
        } else {
            if(ticks > 0) {
                if (!Bllry.MODULE_MANAGER.getModule(SpeedModule.class).isToggled() && !mc.options.jumpKey.isPressed()) Bllry.WORLD_MANAGER.setTimerMultiplier(speed.getValue().floatValue());
                ticks--;
            } else {
                reset();
            }
        }
    }

    public void reset() {
        Bllry.WORLD_MANAGER.setTimerMultiplier(1.0f);
        ticks = 0;
        wait = 0;
    }

    @Override
    public String getMetaData() {
        return (ticks >= maxTicks.getValue().intValue() ? Formatting.GREEN + "" : "") + ticks + Formatting.RESET;
    }
}
