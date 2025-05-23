package me.lily.bllry.modules.impl.miscellaneous;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.TickEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.settings.impl.NumberSetting;
import me.lily.bllry.utils.minecraft.WorldUtils;
import me.lily.bllry.utils.system.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

@RegisterModule(name = "PosLogger", category = Module.Category.MISCELLANEOUS)
public class PosLoggerModule extends Module {
    public NumberSetting delay = new NumberSetting("Delay", "", 1, 0, 5);
    public BooleanSetting showIDs = new BooleanSetting("ShowIDs", "", false);
    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(getNull()) return;
        PlayerEntity target = getTarget();

        if(target != null && timer.hasTimeElapsed(delay.getValue().intValue()*1000)) {
            Bllry.CHAT_MANAGER.message(target.getName().getString() + ", [" + WorldUtils.getMovementDirection(target.getMovementDirection()) + "], X:" + target.getX() + ", Y:" + target.getY() + ", Z:" + target.getZ() + ", Yaw: " + target.getYaw() + ", Pitch: " + target.getPitch());
            timer.reset();
        }

        if(showIDs.getValue()) {
            for(Entity entity : mc.world.getEntities()) {
                entity.setCustomName(Text.literal(entity.getId() + ""));
                entity.setCustomNameVisible(true);
            }
        }
    }

    private PlayerEntity getTarget() {
        PlayerEntity target = null;
        for(PlayerEntity player : mc.world.getPlayers()) {
            if(player == mc.player || player.isDead() || mc.player.distanceTo(player) > 12) continue;

            if(target == null) {
                target = player;
                continue;
            }

            if(mc.player.distanceTo(player) < mc.player.distanceTo(target)) {
                target = player;
            }
        }
        return target;
    }
}
