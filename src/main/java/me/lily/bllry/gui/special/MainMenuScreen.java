package me.lily.bllry.gui.special;

import me.lily.bllry.Bllry;
import me.lily.bllry.utils.IMinecraft;
import me.lily.bllry.utils.color.ColorUtils;
import me.lily.bllry.utils.graphics.Renderer2D;
import me.lily.bllry.utils.system.MathUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.Resource;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.awt.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// TODO: Alt manager and Mods button
public class MainMenuScreen extends Screen implements IMinecraft {
    private final String splashText;
    private final int buttonWidth = 80, buttonHeight = 16;

    public MainMenuScreen() {
        super(Text.literal(Bllry.MOD_ID + "-menu"));

        splashText = getSplashText();
    }

    public boolean shouldPause() {
        return false;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();

        // Background
        Renderer2D.renderQuad(matrices, 0, 0, width, height, new Color(25, 25, 25,255));

        // Gradient
        for(int i = 0; i < width; i++) {
            Color color = ColorUtils.getRainbow(2L, 0.7f, 1.0f, 255, i*5L);
            Renderer2D.renderQuad(matrices, i, 0, i + 1, 1, color);
        }

        // Watermark
        drawText(context, Formatting.WHITE + "Syd" + Formatting.RESET + "ney", width/2f - Bllry.FONT_MANAGER.getWidth("Bllry"), height/2f - Bllry.FONT_MANAGER.getHeight()*2 - 5, 2, ColorUtils.getRainbow(2L, 0.7f, 1.0f, 255, width/2*5L));

        // Time
        String date = new SimpleDateFormat("MM/dd/yy").format(new Date()) + " " + new SimpleDateFormat("hh:mm aa").format(new Date());
        drawText(context, date, width/2f - Bllry.FONT_MANAGER.getWidth(date)/2f, 6, 1, Color.GRAY);

        // Client version
        drawText(context, Bllry.MOD_NAME + " " + Bllry.MOD_VERSION + "-mc" + Bllry.MINECRAFT_VERSION + "+" + Bllry.GIT_REVISION + "." + Bllry.GIT_HASH, 2, height - Bllry.FONT_MANAGER.getHeight() - 2, 1, Color.GRAY);

        drawButton(context, "Singleplayer", width/2f - buttonWidth - 2, height/2f, mouseX, mouseY);
        drawButton(context, "Multiplayer", width/2f, height/2f, mouseX, mouseY);
        drawButton(context, "Game Settings", width/2f + buttonWidth + 2, height/2f, mouseX, mouseY);
        drawButton(context, "Quit Game", width - buttonWidth/2f - 2, height - buttonHeight - 2, mouseX, mouseY);

        // Splash
        drawText(context, splashText, width/2f - Bllry.FONT_MANAGER.getWidth(splashText)/2f, height/2f + buttonHeight + 5f, 1, Color.WHITE);

        // Status
        drawClientStatus(context);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0) {
            if(width/2f - Bllry.FONT_MANAGER.getWidth("Bllry") <= mouseX && height/2f - Bllry.FONT_MANAGER.getHeight()*2 - 5 <= mouseY && width/2f + Bllry.FONT_MANAGER.getWidth("Bllry") > mouseX && height/2f - 5 > mouseY) {
                try {
                    Util.getOperatingSystem().open(new URI("https://youtu.be/INE4RacaApQ?si=ShQU8VjfpgdxW8nb"));
                } catch (Exception ignored) { }
                playClickSound();
            }
            if(isHoveringButton(width/2f - buttonWidth - 2, height/2f, mouseX, mouseY)) {
                mc.setScreen(new SelectWorldScreen(this));
                playClickSound();
            }
            if(isHoveringButton(width/2f, height/2f, mouseX, mouseY)) {
                mc.setScreen(new MultiplayerScreen(this));
                playClickSound();
            }
            if(isHoveringButton(width/2f + buttonWidth + 2, height/2f, mouseX, mouseY)) {
                mc.setScreen(new OptionsScreen(this, mc.options));
                playClickSound();
            }
            if(isHoveringButton(width - buttonWidth/2f - 2, height - buttonHeight - 2, mouseX, mouseY)) {
                mc.scheduleStop();
                playClickSound();
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void drawClientStatus(DrawContext context) {
        if (Bllry.UPDATE_STATUS.equalsIgnoreCase("none")) return;

        String primaryText = "";
        String secondaryText = "";
        Color color = Color.WHITE;

        if (Bllry.UPDATE_STATUS.equalsIgnoreCase("update-available")) {
            secondaryText = "An update is available for Bllry.";
            primaryText = "Please restart the game to apply changes.";
            color = Color.ORANGE;
        }

        if (Bllry.UPDATE_STATUS.equalsIgnoreCase("failed-connection")) {
            secondaryText = "Failed to connect to Bllry's servers.";
            primaryText = "Please make sure you have a working internet connection.";
            color = Color.RED;
        }

        if (Bllry.UPDATE_STATUS.equalsIgnoreCase("failed")) {
            secondaryText = "Failed to update Bllry.";
            primaryText = "Please make sure the auto-updater is working properly.";
            color = Color.RED;
        }

        if (Bllry.UPDATE_STATUS.equalsIgnoreCase("up-to-date")) {
            primaryText = "Bllry is on the latest version.";
        }

        if (primaryText.isEmpty()) return;

        if(!secondaryText.isEmpty()) drawText(context, secondaryText, width/2f - Bllry.FONT_MANAGER.getWidth(secondaryText)/2f, height - 30, 1, color);
        drawText(context, primaryText, width/2f - Bllry.FONT_MANAGER.getWidth(primaryText)/2f, height - 20, 1, color);
    }

    private void drawButton(DrawContext context, String text, float x, float y, int mouseX, int mouseY) {
        Renderer2D.renderQuad(context.getMatrices(), x - buttonWidth/2f, y, x + buttonWidth/2f, y + buttonHeight, isHoveringButton(x, y, mouseX, mouseY) ? new Color(0, 0, 0, 80) : new Color(0, 0, 0, 50));
        drawText(context, text, x - Bllry.FONT_MANAGER.getWidth(text)/2f, y + 4, 1, isHoveringButton(x, y, mouseX, mouseY) ? Color.WHITE : Color.GRAY);
    }

    public boolean isHoveringButton(double x, double y, double mouseX, double mouseY) {
        return x - buttonWidth/2f <= mouseX && y <= mouseY && x + buttonWidth/2f > mouseX && y + buttonHeight > mouseY;
    }

    private void drawText(DrawContext context, String text, float x, float y, float scale, Color color) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(scale, scale, 0);
        Bllry.FONT_MANAGER.drawText(context, text, 0, 0, color);
        context.getMatrices().pop();
    }

    private String getSplashText() {
        String splash = "";
        Identifier identifier = Identifier.of(Bllry.MOD_ID, "splash.txt");

        try {
            Resource resource = mc.getResourceManager().getResource(identifier).orElseThrow();
            List<String> messages = resource.getReader().lines().toList();
            splash = messages.get((int) MathUtils.random(messages.size(), 0));
        } catch (Exception ignored) { }

        return splash;
    }

    private void playClickSound() {
        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }
}
