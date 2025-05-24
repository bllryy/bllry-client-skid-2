package me.lily.bllry.mixins;

import me.lily.bllry.Bllry;
import me.lily.bllry.modules.impl.core.FontModule;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    @Inject(method = "drawLayer(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;IIZ)F", at = @At("HEAD"), cancellable = true)
    private void drawLayer$String(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType layerType, int backgroundColor, int light, boolean swapZIndex, CallbackInfoReturnable<Float> info) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && Bllry.MODULE_MANAGER.getModule(FontModule.class).global.getValue()) {
            MatrixStack matrices = new MatrixStack();

            matrices.push();
            matrices.multiplyPositionMatrix(matrix);

            if (shadow) Bllry.FONT_MANAGER.getFontRenderer().drawString(matrices, text, x + Bllry.FONT_MANAGER.getShadowOffset(), y + Bllry.FONT_MANAGER.getShadowOffset(), color, true);
            Bllry.FONT_MANAGER.getFontRenderer().drawString(matrices, text, x, y, color, false);

            matrices.pop();

            info.setReturnValue(x + Bllry.FONT_MANAGER.getFontRenderer().getTextWidth(Text.literal(text).asOrderedText()));
        }
    }

    @Inject(method = "drawLayer(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;IIZ)F", at = @At("HEAD"), cancellable = true)
    private void drawLayer$OrderedText(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType layerType, int underlineColor, int light, boolean swapZIndex, CallbackInfoReturnable<Float> info) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && Bllry.MODULE_MANAGER.getModule(FontModule.class).global.getValue()) {
            MatrixStack matrices = new MatrixStack();

            matrices.push();
            matrices.multiplyPositionMatrix(matrix);

            if (shadow) Bllry.FONT_MANAGER.getFontRenderer().drawText(matrices, text, x + Bllry.FONT_MANAGER.getShadowOffset(), y + Bllry.FONT_MANAGER.getShadowOffset(), color, true);
            Bllry.FONT_MANAGER.getFontRenderer().drawText(matrices, text, x, y, color, false);

            matrices.pop();

            info.setReturnValue(x + Bllry.FONT_MANAGER.getFontRenderer().getTextWidth(text));
        }
    }

    @Inject(method = "getWidth(Ljava/lang/String;)I", at = @At("HEAD"), cancellable = true)
    private void getWidth(String text, CallbackInfoReturnable<Integer> info) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && Bllry.MODULE_MANAGER.getModule(FontModule.class).global.getValue()) {
            info.setReturnValue(Bllry.FONT_MANAGER.getWidth(text));
        }
    }

    @Inject(method = "getWidth(Lnet/minecraft/text/StringVisitable;)I", at = @At("HEAD"), cancellable = true)
    private void getWidth(StringVisitable text, CallbackInfoReturnable<Integer> info) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && Bllry.MODULE_MANAGER.getModule(FontModule.class).global.getValue()) {
            info.setReturnValue(Bllry.FONT_MANAGER.getWidth(text.getString()));
        }
    }

    @Inject(method = "getWidth(Lnet/minecraft/text/OrderedText;)I", at = @At("HEAD"), cancellable = true)
    private void getWidth(OrderedText text, CallbackInfoReturnable<Integer> info) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && Bllry.MODULE_MANAGER.getModule(FontModule.class).global.getValue()) {
            info.setReturnValue((int) Bllry.FONT_MANAGER.getFontRenderer().getTextWidth(text));
        }
    }
}