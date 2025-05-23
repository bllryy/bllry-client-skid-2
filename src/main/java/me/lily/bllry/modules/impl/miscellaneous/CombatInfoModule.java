package me.lily.bllry.modules.impl.miscellaneous;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.RenderOverlayEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.modules.impl.combat.AutoCrystalModule;
import me.lily.bllry.modules.impl.combat.KillAuraModule;
import me.lily.bllry.modules.impl.core.HUDModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.settings.impl.StringSetting;
import me.lily.bllry.utils.minecraft.HoleUtils;
import net.minecraft.item.Items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RegisterModule(name = "CombatInfo", description = "Shows useful combat information on screen.", category = Module.Category.MISCELLANEOUS)
public class CombatInfoModule extends Module {
    public StringSetting watermark = new StringSetting("Watermark", "The client name that will be rendered.", Bllry.MOD_NAME);
    public BooleanSetting version = new BooleanSetting("Version", "Renders the client's version after the name.", true);

    @SubscribeEvent
    public void onRenderOverlay(RenderOverlayEvent event) {
        if(getNull()) return;

        HUDModule hudModule = Bllry.MODULE_MANAGER.getModule(HUDModule.class);

        List<InfoEntry> entries = new ArrayList<>();
        AutoCrystalModule ca = Bllry.MODULE_MANAGER.getModule(AutoCrystalModule.class);
        KillAuraModule ka = Bllry.MODULE_MANAGER.getModule(KillAuraModule.class);
        int y = mc.getWindow().getScaledHeight() / 2, totems = mc.player.getInventory().count(Items.TOTEM_OF_UNDYING);

        entries.add(new InfoEntry(watermark.getValue() + (version.getValue() ? " " + Bllry.MOD_VERSION : ""), null));
        entries.add(new InfoEntry("HTR", getColor(ka.getTarget() != null && mc.player.distanceTo(ka.getTarget()) < ka.range.getValue().floatValue())));
        entries.add(new InfoEntry("PLR", getColor(ca.getTarget() != null && mc.player.distanceTo(ca.getTarget()) < ca.enemyRange.getValue().floatValue())));
        entries.add(new InfoEntry(totems + "", getColor(totems > 0)));
        entries.add(new InfoEntry("PING " + Bllry.SERVER_MANAGER.getPing(), getColor(Bllry.SERVER_MANAGER.getPing() < 100)));
        entries.add(new InfoEntry("LBY", getColor(HoleUtils.isPlayerInHole(mc.player))));

        int offset = 0;
        for(InfoEntry entry : entries) {
            hudModule.drawText(event.getContext(), entry.text(), 2, y - (3* Bllry.FONT_MANAGER.getHeight()) + (offset* Bllry.FONT_MANAGER.getHeight()), entry.text().startsWith(watermark.getValue()) && hudModule.colorMode.getValue().equals("Rainbow") && hudModule.rainbowMode.getValue().equals("Horizontal"), entry.color());
            offset++;
        }
    }

    private Color getColor(boolean toggled) {
        return toggled ? new Color(100, 255, 100) : new Color(255, 100, 100);
    }

    public record InfoEntry(String text, Color color) {}
}
