package me.lily.bllry.utils.chat;

import me.lily.bllry.Bllry;
import me.lily.bllry.modules.impl.core.CommandsModule;
import me.lily.bllry.utils.text.FormattingUtils;
import net.minecraft.util.StringIdentifiable;

public class ChatUtils {
    public static StringIdentifiable getPrimary() {
        return FormattingUtils.getFormatting(Bllry.MODULE_MANAGER.getModule(CommandsModule.class).primaryMessageColor.getValue());
    }

    public static StringIdentifiable getSecondary() {
        return FormattingUtils.getFormatting(Bllry.MODULE_MANAGER.getModule(CommandsModule.class).secondaryMessageColor.getValue());
    }
}
