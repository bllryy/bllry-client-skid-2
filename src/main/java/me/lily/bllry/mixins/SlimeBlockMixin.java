package me.lily.bllry.mixins;

import me.lily.bllry.Bllry;
import me.lily.bllry.modules.impl.movement.NoSlowModule;
import me.lily.bllry.utils.IMinecraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin implements IMinecraft {
    @Inject(method = "onSteppedOn", at = @At("HEAD"), cancellable = true)
    private void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo info) {
        if (entity == mc.player && Bllry.MODULE_MANAGER.getModule(NoSlowModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(NoSlowModule.class).slimeBlocks.getValue()) {
            info.cancel();
        }
    }
}
