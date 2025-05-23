package me.lily.bllry.mixins;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.impl.EntitySpawnEvent;
import me.lily.bllry.modules.impl.visuals.AtmosphereModule;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Integer> info) {
        if (Bllry.MODULE_MANAGER.getModule(AtmosphereModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(AtmosphereModule.class).modifyFog.getValue()) {
            info.setReturnValue(Bllry.MODULE_MANAGER.getModule(AtmosphereModule.class).fogColor.getColor().getRGB());
        }
    }

    @Inject(method = "addEntity", at = @At(value = "HEAD"))
    private void addEntity(Entity entity, CallbackInfo info) {
        Bllry.EVENT_HANDLER.post(new EntitySpawnEvent(entity));
    }
}
