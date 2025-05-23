package me.lily.bllry.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.lily.bllry.Bllry;
import me.lily.bllry.modules.impl.miscellaneous.NameProtectModule;
import me.lily.bllry.utils.IMinecraft;
import me.lily.bllry.utils.text.CustomFormatting;
import me.lily.bllry.utils.text.FormattingUtils;
import net.minecraft.text.Style;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextVisitFactory.class)
public class TextVisitFactoryMixin implements IMinecraft {
    @WrapOperation(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 1))
    private static char visitFormatted(String instance, int index, Operation<Character> original, @Local(ordinal = 2) LocalRef<Style> style) {
        CustomFormatting customFormatting = CustomFormatting.byCode(instance.charAt(index));
        if (customFormatting != null) style.set(FormattingUtils.withExclusiveFormatting(style.get(), customFormatting));

        return original.call(instance, index);
    }

    @ModifyVariable(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static String replaceText(String value) {
        try {
            // Check if the module manager is initialized
            if (Bllry.MODULE_MANAGER == null) {
                return value;
            }

            // Get the module
            NameProtectModule module = Bllry.MODULE_MANAGER.getModule(NameProtectModule.class);
            if (module == null) {
                return value;
            }

            // Check if module is enabled (try different method names based on your module system)
            if (!module.isToggled()) { // Change this to: module.isEnabled() or module.getState() as needed
                return value;
            }

            // Check if Minecraft client is available
            if (mc == null || mc.getSession() == null) {
                return value;
            }

            // Get the name value (try different method names based on your StringSetting)
            String nameValue = module.name.getValue(); // Change this to: module.name.get() or module.name.value as needed
            if (nameValue == null || nameValue.isEmpty()) {
                return value;
            }

            return value.replaceAll(mc.getSession().getUsername(), nameValue);

        } catch (Exception e) {
            // If anything goes wrong, just return the original value to prevent crashes
            return value;
        }
    }
}