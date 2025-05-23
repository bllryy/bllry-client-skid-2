package me.lily.bllry.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.lily.bllry.Bllry;
import me.lily.bllry.modules.impl.visuals.NoRenderModule;
import net.minecraft.entity.LimbAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LimbAnimator.class)
public class LimbAnimatorMixin {
    @ModifyReturnValue(method = "getPos()F", at = @At("RETURN"))
    private float getPos(float original) {
        if(Bllry.MODULE_MANAGER.getModule(NoRenderModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(NoRenderModule.class).limbSwing.getValue()) {
            return 0;
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "getPos(F)F", at = @At("RETURN"))
    private float getPos2(float original) {
        if(Bllry.MODULE_MANAGER.getModule(NoRenderModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(NoRenderModule.class).limbSwing.getValue()) {
            return 0;
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "getSpeed()F", at = @At("RETURN"))
    private float getSpeed(float original) {
        if(Bllry.MODULE_MANAGER.getModule(NoRenderModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(NoRenderModule.class).limbSwing.getValue()) {
            return 0;
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "getSpeed(F)F", at = @At("RETURN"))
    private float getSpeed2(float original) {
        if(Bllry.MODULE_MANAGER.getModule(NoRenderModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(NoRenderModule.class).limbSwing.getValue()) {
            return 0;
        } else {
            return original;
        }
    }
}
