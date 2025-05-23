package me.lily.bllry.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.lily.bllry.Bllry;
import me.lily.bllry.modules.impl.movement.VelocityModule;
import me.lily.bllry.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin implements IMinecraft {
    @WrapOperation(method = "handleStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;pullHookedEntity(Lnet/minecraft/entity/Entity;)V"))
    private void pushOutOfBlocks(FishingBobberEntity instance, Entity entity, Operation<Void> original) {
        if (entity == mc.player && Bllry.MODULE_MANAGER.getModule(VelocityModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(VelocityModule.class).antiFishingRod.getValue())
            return;

        original.call(instance, entity);
    }
}
