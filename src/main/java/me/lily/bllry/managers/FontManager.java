package me.lily.bllry.managers;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.Setter;
import me.lily.bllry.Bllry;
import me.lily.bllry.mixins.accessors.DrawContextAccessor;
import me.lily.bllry.mixins.accessors.TextRendererAccessor;
import me.lily.bllry.modules.impl.core.FontModule;
import me.lily.bllry.utils.IMinecraft;
import me.lily.bllry.utils.color.ColorUtils;
import me.lily.bllry.utils.font.FontRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;

import java.awt.*;

@Getter @Setter
public class FontManager implements IMinecraft {
    private FontRenderer fontRenderer;

    public void drawText(DrawContext context, String text, int x, int y, Color color) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && fontRenderer != null) { // != null
            fontRenderer.drawString(context.getMatrices(), text, x, y, color.getRGB(), false);
        } else {
            context.drawText(mc.textRenderer, text, x, y, color.getRGB(), false);
        }
    }

    public void drawTextWithShadow(DrawContext context, String text, int x, int y, Color color) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && fontRenderer != null) { // != null
            if (!Bllry.MODULE_MANAGER.getModule(FontModule.class).shadowMode.getValue().equalsIgnoreCase("None")) fontRenderer.drawString(context.getMatrices(), text, x + getShadowOffset(), y + getShadowOffset(), color.getRGB(), true);
            fontRenderer.drawString(context.getMatrices(), text, x, y, color.getRGB(), false);
        } else {
            context.drawText(mc.textRenderer, text, x, y, color.getRGB(), true);
        }
    }

    public void drawText(DrawContext context, OrderedText text, int x, int y, Color color) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && fontRenderer != null) {
            fontRenderer.drawText(context.getMatrices(), text, x, y, color.getRGB(), false);
        } else {
            context.drawText(mc.textRenderer, text, x, y, color.getRGB(), false);
        }
    }

    public void drawTextWithShadow(DrawContext context, OrderedText text, int x, int y, Color color) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && fontRenderer != null) {
            if (!Bllry.MODULE_MANAGER.getModule(FontModule.class).shadowMode.getValue().equalsIgnoreCase("None")) fontRenderer.drawText(context.getMatrices(), text, x + getShadowOffset(), y + getShadowOffset(), color.getRGB(), true);
            fontRenderer.drawText(context.getMatrices(), text, x, y, color.getRGB(), false);
        } else {
            context.drawText(mc.textRenderer, text, x, y, color.getRGB(), true);
        }
    }

    public void drawTextWithShadow(MatrixStack matrices, String text, int x, int y, VertexConsumerProvider vertexConsumers, Color color) {
        RenderSystem.disableDepthTest();

        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && fontRenderer != null) {
            if (!Bllry.MODULE_MANAGER.getModule(FontModule.class).shadowMode.getValue().equalsIgnoreCase("None")) fontRenderer.drawString(matrices, text, x + getShadowOffset(), y + getShadowOffset(), color.getRGB(), true);
            fontRenderer.drawString(matrices, text, x, y, color.getRGB(), false);
        } else {
            ((TextRendererAccessor) mc.textRenderer).invokeDrawLayer(text, x, y, TextRendererAccessor.invokeTweakTransparency(color.getRGB()), true, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0, false);
            mc.getBufferBuilders().getEntityVertexConsumers().draw();

            ((TextRendererAccessor) mc.textRenderer).invokeDrawLayer(text, x, y, TextRendererAccessor.invokeTweakTransparency(color.getRGB()), false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0, false);
            mc.getBufferBuilders().getEntityVertexConsumers().draw();
        }

        RenderSystem.enableDepthTest();
    }

    public void drawTextWithOutline(DrawContext context, String text, int x, int y, Color color, Color outlineColor) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && fontRenderer != null) {
            fontRenderer.drawString(context.getMatrices(), FontRenderer.stripControlCodes(text), x + 0.5f, y - 0.5f, outlineColor.getRGB(), false);
            fontRenderer.drawString(context.getMatrices(), FontRenderer.stripControlCodes(text), x - 0.5f, y + 0.5f, outlineColor.getRGB(), false);
            fontRenderer.drawString(context.getMatrices(), FontRenderer.stripControlCodes(text), x + 0.5f, y + 0.5f, outlineColor.getRGB(), false);
            fontRenderer.drawString(context.getMatrices(), FontRenderer.stripControlCodes(text), x - 0.5f, y - 0.5f, outlineColor.getRGB(), false);

            fontRenderer.drawString(context.getMatrices(), text, x, y, color.getRGB(), false);
        } else {
            mc.textRenderer.drawWithOutline(Text.literal(text).asOrderedText(), mc.getWindow().getScaledWidth() / 2.0f - Bllry.FONT_MANAGER.getWidth(text) / 2.0f, mc.getWindow().getScaledHeight() / 2.0f + 16, color.getRGB(), outlineColor.getRGB(), context.getMatrices().peek().getPositionMatrix(), ((DrawContextAccessor) context).getVertexConsumers(), 0);
        }
    }

    public void drawRainbowString(DrawContext context, String string, int x, int y, long offset) {
        MutableText builder = Text.empty();

        int[] i = {0};
        Text.literal(string).asOrderedText().accept((index, style, codePoint) -> {
            MutableText text = Text.empty();

            if (style.getColor() == null) {
                long index1 = (long) i[0] * offset;
                Color color = ColorUtils.getOffsetRainbow(index1);
                text.append(Text.literal(String.valueOf(Character.toChars(codePoint))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color.getRGB()))));
            } else {
                text.append(Text.literal(String.valueOf(Character.toChars(codePoint))).setStyle(style));
            }

            builder.append(text);
            i[0]++;

            return true;
        });

        Bllry.FONT_MANAGER.drawTextWithShadow(context, builder.asOrderedText(), x, y, Color.WHITE);
    }

    public int getWidth(String text) {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && fontRenderer != null) {
            return (int) fontRenderer.getTextWidth(Text.literal(text).asOrderedText()) + Bllry.MODULE_MANAGER.getModule(FontModule.class).widthOffset.getValue().intValue();

        } else {
            return mc.textRenderer.getWidth(text);
        }
    }

    public int getHeight() {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).isToggled() && Bllry.MODULE_MANAGER.getModule(FontModule.class).customFont.getValue() && fontRenderer != null) {
            return (int) fontRenderer.getHeight() + Bllry.MODULE_MANAGER.getModule(FontModule.class).heightOffset.getValue().intValue();
        } else {
            return mc.textRenderer.fontHeight;
        }
    }

    public float getShadowOffset() {
        if (Bllry.MODULE_MANAGER.getModule(FontModule.class).shadowMode.getValue().equalsIgnoreCase("None")) {
            return 0.0f;
        } else if (Bllry.MODULE_MANAGER.getModule(FontModule.class).shadowMode.getValue().equalsIgnoreCase("Custom")) {
            return Bllry.MODULE_MANAGER.getModule(FontModule.class).shadowOffset.getValue().floatValue();
        } else {
            return 1.0f;
        }
    }

    public boolean hasFont(String name) {
        for (String fontName : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) if (fontName.equalsIgnoreCase(name)) return true;
        return false;
    }


}
