package me.lily.bllry.commands.impl;

import me.lily.bllry.Bllry;
import me.lily.bllry.commands.Command;
import me.lily.bllry.commands.RegisterCommand;
import net.minecraft.util.Util;

import java.io.File;

@RegisterCommand(name = "folder", description = "Opens the clients folder.")
public class FolderCommand extends Command {
    @Override
    public void execute(String[] args) {
        File folder = new File(Bllry.MOD_NAME);
        if (folder.exists()) {
            Util.getOperatingSystem().open(folder);
        } else {
            Bllry.CHAT_MANAGER.info("Could not find the client's configuration folder.");
        }
    }
}
