package me.lily.bllry.utils.minecraft;

import com.mojang.authlib.GameProfile;
import me.lily.bllry.events.impl.RenderWorldEvent;
import me.lily.bllry.mixins.accessors.LimbAnimatorAccessor;
import me.lily.bllry.utils.IMinecraft;
import me.lily.bllry.utils.graphics.ModelRenderer;
import me.lily.bllry.utils.mixins.ILivingEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.util.UUID;

public class StaticPlayerEntity extends OtherClientPlayerEntity implements IMinecraft {
    public StaticPlayerEntity(PlayerEntity player) {
        super(mc.world, new GameProfile(UUID.randomUUID(), player.getName().getString()));

        ((ILivingEntity) this).sydney$setStaticPlayerEntity(true);

        copyPositionAndRotation(player);
        prevYaw = getYaw();
        prevPitch = getPitch();
        headYaw = player.headYaw;
        prevHeadYaw = headYaw;
        bodyYaw = player.bodyYaw;
        prevBodyYaw = bodyYaw;

        setSneaking(player.isSneaking());

        limbAnimator.setSpeed(player.limbAnimator.getSpeed());
        ((LimbAnimatorAccessor) limbAnimator).setPos(player.limbAnimator.getPos());

        setPose(player.getPose());
    }

    public void render(RenderWorldEvent event, boolean fill, Color fillColor, boolean outline, Color outlineColor) {
        ModelRenderer.renderModel(this, true, 1.0f, event.getTickDelta(), new ModelRenderer.Render(fill, fillColor, outline, outlineColor, false));
    }
}
