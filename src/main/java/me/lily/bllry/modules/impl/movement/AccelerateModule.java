package me.lily.bllry.modules.impl.movement;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.PlayerMoveEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.utils.minecraft.MovementUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2d;

@RegisterModule(name = "Accelerate", description = "Gives you more precise movement instantly.", category = Module.Category.MOVEMENT)
public class AccelerateModule extends Module {
    public BooleanSetting air = new BooleanSetting("Air", "Increases your speed while off ground.", true);
    public BooleanSetting speedInWater = new BooleanSetting("SpeedInWater", "Increases your speed while in water.", false);

    @SubscribeEvent
    public void onPlayerMove(PlayerMoveEvent event) {
        if(getNull() || (Bllry.MODULE_MANAGER.getModule(HoleSnapModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(HoleSnapModule.class).hole != null) || Bllry.MODULE_MANAGER.getModule(SpeedModule.class).isToggled()) return;

        if (mc.player.fallDistance >= 5.0f || mc.player.isSneaking() || mc.player.isClimbing() || mc.world.getBlockState(mc.player.getBlockPos()).getBlock() == Blocks.COBWEB || mc.player.getAbilities().flying || mc.player.isGliding())
            return;

        if(!mc.player.isOnGround() && !air.getValue()) return;

        if((mc.player.isTouchingWater() || mc.player.isInLava()) && !speedInWater.getValue()) return;

        Vector2d velocity = MovementUtils.forward(MovementUtils.getPotionSpeed(MovementUtils.DEFAULT_SPEED));
        event.setMovement(new Vec3d(velocity.x, event.getMovement().getY(), event.getMovement().getZ()));
        event.setMovement(new Vec3d(event.getMovement().getX(), event.getMovement().getY(), velocity.y));
        event.setCancelled(true);
    }
}
