package me.lily.bllry.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.lily.bllry.Bllry;
import me.lily.bllry.events.impl.ConsumeItemEvent;
import me.lily.bllry.events.impl.PlayerJumpEvent;
//import me.aidan.sydney.modules.impl.movement.*;
import me.lily.bllry.modules.impl.movement.*;
import me.lily.bllry.modules.impl.player.SwingModule;
import me.lily.bllry.utils.IMinecraft;
import me.lily.bllry.utils.mixins.ILivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntity, IMinecraft {
    @Shadow public abstract @Nullable EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

    @Shadow @Final private static EntityAttributeModifier SPRINTING_SPEED_BOOST;

    @Shadow private int jumpingCooldown;

    @Shadow protected ItemStack activeItemStack;

    @Unique private boolean staticPlayerEntity = false;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean sydney$isStaticPlayerEntity() {
        return staticPlayerEntity;
    }

    @Override
    public void sydney$setStaticPlayerEntity(boolean staticPlayerEntity) {
        this.staticPlayerEntity = staticPlayerEntity;
    }

    @WrapMethod(method = "getStepHeight")
    private float getStepHeight(Operation<Float> original) {
        if ((Object) this == mc.player && Bllry.MODULE_MANAGER != null && ((Bllry.MODULE_MANAGER.getModule(StepModule.class).isToggled() && mc.player.isOnGround()) || (Bllry.MODULE_MANAGER.getModule(HoleSnapModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(HoleSnapModule.class).step.getValue()) /*|| Bllry.MODULE_MANAGER.getModule(SydneyRoboticsModule.class).shouldStep()*/)) {
            return Bllry.MODULE_MANAGER.getModule(StepModule.class).height.getValue().floatValue();
        }

        return original.call();
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 2, shift = At.Shift.BEFORE))
    private void doItemUse(CallbackInfo info) {
        if (Bllry.MODULE_MANAGER != null && Bllry.MODULE_MANAGER.getModule(NoJumpDelayModule.class).isToggled() && jumpingCooldown == 10) {
            jumpingCooldown = Bllry.MODULE_MANAGER.getModule(NoJumpDelayModule.class).ticks.getValue().intValue();
        }
    }

    @ModifyConstant(method = "getHandSwingDuration", constant = @Constant(intValue = 6))
    private int getHandSwingDuration(int constant) {
        if ((Object) this != mc.player) return constant;
        return Bllry.MODULE_MANAGER.getModule(SwingModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(SwingModule.class).modifySpeed.getValue() && mc.options.getPerspective().isFirstPerson() ? (21 - Bllry.MODULE_MANAGER.getModule(SwingModule.class).speed.getValue().intValue()) : constant;
    }

    @Inject(method = "consumeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER))
    private void consumeItem(CallbackInfo ci) {
        if((Object) this == mc.player) Bllry.EVENT_HANDLER.post(new ConsumeItemEvent(activeItemStack));
    }

    @Inject(method = "setSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setSprinting(Z)V", shift = At.Shift.AFTER), cancellable = true)
    private void setSprinting$setSprinting(boolean sprinting, CallbackInfo info) {
        if ((Object) this == mc.player && Bllry.MODULE_MANAGER.getModule(SprintModule.class).isToggled()) {
            EntityAttributeInstance entityAttributeInstance = getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
            entityAttributeInstance.removeModifier(SPRINTING_SPEED_BOOST.id());

            if (Bllry.MODULE_MANAGER.getModule(SprintModule.class).shouldSprint()) {
                setFlag(3, true);
                entityAttributeInstance.addTemporaryModifier(SPRINTING_SPEED_BOOST);
            } else {
                setFlag(3, false);
            }

            info.cancel();
        }
    }

    @ModifyExpressionValue(method = "travelMidAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/effect/StatusEffectInstance;"))
    private @Nullable StatusEffectInstance travelMidAir$getStatusEffect(@Nullable StatusEffectInstance original) {
        if (Bllry.MODULE_MANAGER.getModule(AntiLevitationModule.class).isToggled()) return null;
        return original;
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z", ordinal = 1))
    private boolean tickMovement$hasStatusEffect(boolean original) {
        if (Bllry.MODULE_MANAGER.getModule(AntiLevitationModule.class).isToggled()) return false;
        return original;
    }

    @Inject(method = "jump", at = @At("HEAD"))
    private void jump$HEAD(CallbackInfo info) {
        if ((Object) this != mc.player) return;
        Bllry.EVENT_HANDLER.post(new PlayerJumpEvent());
    }

    @Inject(method = "jump", at = @At("RETURN"))
    private void jump$RETURN(CallbackInfo info) {
        if ((Object) this != mc.player) return;
        Bllry.EVENT_HANDLER.post(new PlayerJumpEvent.Post());
    }
}
