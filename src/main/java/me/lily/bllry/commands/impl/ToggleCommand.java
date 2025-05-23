package me.lily.bllry.commands.impl;

import me.lily.bllry.Bllry;
import me.lily.bllry.commands.Command;
import me.lily.bllry.commands.RegisterCommand;
import me.lily.bllry.modules.Module;
import me.lily.bllry.settings.Setting;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.utils.chat.ChatUtils;
import net.minecraft.util.Formatting;

@RegisterCommand(name = "toggle", tag = "Toggle", description = "Toggles a specified module or a setting on and off.", syntax = "<[module]> | <[module]> <[setting]>", aliases = {"t"})
public class ToggleCommand extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length == 1 || args.length == 2) {
            Module module = Bllry.MODULE_MANAGER.getModule(args[0]);
            if (module == null) {
                Bllry.CHAT_MANAGER.tagged("Could not find the module specified.", getTag(), getName());
                return;
            }

            if (args.length == 1) {
                if (module.isPersistent()) {
                    Bllry.CHAT_MANAGER.tagged("Cannot toggle a persistent module.", getTag(), getName());
                    return;
                }

                module.setToggled(!module.isToggled(), false);
                Bllry.CHAT_MANAGER.tagged(ChatUtils.getPrimary() + module.getName() + ChatUtils.getSecondary() + " has been toggled " + (module.isToggled() ? Formatting.GREEN + "on" : Formatting.RED + "off") + ChatUtils.getSecondary() + ".", getTag(), getName() + "-cmd-" + module.getName());
            }

            if (args.length == 2) {
                Setting setting = module.getSetting(args[1]);
                if (setting == null) {
                    Bllry.CHAT_MANAGER.tagged("Could not find the setting specified.", getTag(), getName());
                    return;
                }

                if (!(setting instanceof BooleanSetting booleanSetting)) {
                    Bllry.CHAT_MANAGER.tagged("This command only works for " + ChatUtils.getPrimary() + "boolean" + ChatUtils.getSecondary() + " settings.", getTag(), getName());
                    return;
                }

                booleanSetting.setValue(!booleanSetting.getValue());
                Bllry.CHAT_MANAGER.tagged(ChatUtils.getPrimary() + setting.getName() + ChatUtils.getSecondary() + " has been toggled " + (booleanSetting.getValue() ? Formatting.GREEN + "on" : Formatting.RED + "off") + ChatUtils.getSecondary() + " for " + ChatUtils.getPrimary() + module.getName() + ChatUtils.getSecondary() + ".", getTag(), getName() + "-cmd-" + module.getName() + "-" + setting.getName() );

            }
        } else {
            messageSyntax();
        }
    }
}
