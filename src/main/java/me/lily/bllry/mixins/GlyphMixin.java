package me.lily.bllry.mixins;

import me.lily.bllry.Bllry;
import me.lily.bllry.modules.impl.core.FontModule;
import net.minecraft.client.font.Glyph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Glyph.class)
public interface GlyphMixin {
    @Inject(method = "getShadowOffset", at = @At("HEAD"), cancellable = true)
    private void getShadowOffset(CallbackInfoReturnable<Float> info) {
        if (Bllry.MODULE_MANAGER != null && Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && !Bllry.MODULE_MANAGER.getModule(FontModule.class).shadowMode.getValue().equalsIgnoreCase("Default")) {
            info.setReturnValue(Bllry.FONT_MANAGER.getShadowOffset());
        }
    }
}
