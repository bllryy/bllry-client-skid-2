package me.lily.bllry.commands.impl;

import me.lily.bllry.Bllry;
import me.lily.bllry.commands.Command;
import me.lily.bllry.commands.RegisterCommand;
import me.lily.bllry.modules.Module;
import me.lily.bllry.utils.chat.ChatUtils;
import net.minecraft.util.Formatting;

import java.util.List;

@RegisterCommand(name = "modules", tag = "Modules", description = "Shows you a list of all of the client's modules and their toggle status.", aliases = {"mods"})
public class ModulesCommand extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            List<Module> modules = Bllry.MODULE_MANAGER.getModules();

            if (modules.isEmpty()) {
                Bllry.CHAT_MANAGER.tagged("There are currently no registered modules.", getTag(), getName());
            } else {
                StringBuilder builder = new StringBuilder();
                int index = 0;

                for (Module module : modules) {
                    index++;
                    builder.append(ChatUtils.getSecondary()).append(module.getName())
                            .append(ChatUtils.getPrimary()).append(" [")
                            .append(module.isToggled() ? Formatting.GREEN + "ON" : Formatting.RED + "OFF")
                            .append(ChatUtils.getPrimary()).append("]")
                            .append(index == modules.size() ? "" : ", ");
                }

                Bllry.CHAT_MANAGER.message("Modules " + ChatUtils.getPrimary() + "[" + ChatUtils.getSecondary() + modules.size() + ChatUtils.getPrimary() + "]: " + ChatUtils.getSecondary() + builder, getName());
            }
        } else {
            messageSyntax();
        }
    }
}
