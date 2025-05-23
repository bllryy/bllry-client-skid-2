package me.lily.bllry.modules.impl.movement;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.PlayerUpdateEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.NumberSetting;

@RegisterModule(name = "ReverseStep", description = "Makes it so that you fall down instantly at a specified height.", category = Module.Category.MOVEMENT)
public class ReverseStepModule extends Module {
    public NumberSetting height = new NumberSetting("Height", "The maximum height at which instant falling will be applied to.", 3.0, 0.0, 12.0);

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!Bllry.SERVER_MANAGER.getSetbackTimer().hasTimeElapsed(300L)) return;
        if (mc.player.isRiding() || mc.player.isGliding() || mc.player.isHoldingOntoLadder() || mc.player.isInLava() || mc.player.isTouchingWater() || mc.player.input.playerInput.jump() || mc.player.input.playerInput.sneak()) {
            return;
        }

        if (mc.player.isOnGround() && nearBlock(height.getValue().doubleValue())) {
            mc.player.setVelocity(mc.player.getVelocity().getX(), -height.getValue().doubleValue(), mc.player.getVelocity().getZ());
        }
    }

    @Override
    public String getMetaData() {
        return String.valueOf(height.getValue().floatValue());
    }

    private boolean nearBlock(double height) {
        for (double i = 0; i < height + 0.5; i += 0.01) {
            if (!mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0, -i, 0))) return true;
        }

        return false;
    }
}
