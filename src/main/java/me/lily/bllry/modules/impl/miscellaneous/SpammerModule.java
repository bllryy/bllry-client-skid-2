package me.lily.bllry.modules.impl.miscellaneous;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.TickEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.settings.impl.NumberSetting;
import me.lily.bllry.settings.impl.StringSetting;
import me.lily.bllry.utils.system.FileUtils;
import me.lily.bllry.utils.system.MathUtils;
import me.lily.bllry.utils.system.Timer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RegisterModule(name = "Spammer", description = "Spams messages in chat from a text file.", category = Module.Category.MISCELLANEOUS)
public class SpammerModule extends Module {
    public StringSetting fileName = new StringSetting("FileName", "The name of the spammer text file.", "spammer.txt");
    public NumberSetting delay = new NumberSetting("Delay", "The delay for the announcer.", 5, 0, 30);
    public BooleanSetting greenText = new BooleanSetting("GreenText", "Makes your message green.", false);
    public BooleanSetting shuffled = new BooleanSetting("Shuffled", "Sends the spammer messages out of order.", false);

    private final Timer timer = new Timer();
    private List<String> messages = new ArrayList<>();
    private int line;

    @Override
    public void onEnable() {
        line = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(getNull()) return;

        File file = new File(Bllry.MOD_NAME + "/Client/" + fileName.getValue());
        messages = FileUtils.readLines(file);

        if(!messages.isEmpty() && timer.hasTimeElapsed(delay.getValue().intValue() * 1000)) {
            if(line >= messages.size()) line = 0;

            String message = shuffled.getValue() ? messages.get((int) MathUtils.random(messages.size(), 0)) : messages.get(line);

            mc.player.networkHandler.sendChatMessage((greenText.getValue() ? "> " : "") + message);
            line++;
            timer.reset();
        }
    }
}
