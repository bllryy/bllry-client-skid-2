package me.lily.bllry.modules.impl.core;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.MouseInputEvent;
import me.lily.bllry.events.impl.SettingChangeEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.settings.impl.StringSetting;
import me.lily.bllry.utils.chat.ChatUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

@RegisterModule(name = "Friend", description = "Allows you to manage the client's friend system.", category = Module.Category.CORE, persistent = true, drawn = false)
public class FriendModule extends Module {
    public BooleanSetting middleClick = new BooleanSetting("MiddleClick", "Adds whichever entity you middle click to your friends list.", true);
    public BooleanSetting friendlyFire = new BooleanSetting("FriendlyFire", "Disables the friend system and allows the attacking of friends.",false);
    public BooleanSetting friendMessage = new BooleanSetting("FriendMessage", "Sends a message to a player whenever you add them to your friends list.", false);
    public StringSetting content = new StringSetting("Content", "The message that will be sent to the people you add. $player is placeholder for their name.", new BooleanSetting.Visibility(friendMessage, true), "/msg $player I have just added you to my friends list!");

    public void sendFriendMessage(String name) {
        if (mc.world == null || mc.player == null) return;
        if (!friendMessage.getValue()) return;

        if (mc.getNetworkHandler().getPlayerList().stream().noneMatch(player -> player.getProfile().getName().equalsIgnoreCase(name))) return;

        if (content.getValue().startsWith("/")) {
            mc.getNetworkHandler().sendChatCommand(content.getValue().substring(1).replace("$player", name));
        } else {
            mc.getNetworkHandler().sendChatMessage(content.getValue().replace("$player", name));
        }
    }

    @SubscribeEvent
    public void onSettingChange(SettingChangeEvent event) {
        if (event.getSetting().equals(friendlyFire)) {
            Bllry.CHAT_MANAGER.warn(ChatUtils.getPrimary() + "Friendly fire" + ChatUtils.getSecondary() + " has been turned " + (friendlyFire.getValue() ? Formatting.GREEN + "on" + Formatting.RESET + ". Friends will now be attacked." : Formatting.RED + "off" + Formatting.RESET + ". Friends will now no longer be attacked."));
        }
    }

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) {
        if (!middleClick.getValue()) return;
        if (event.getButton() != 2)  return;
        if (mc.targetedEntity == null) return;
        if (!(mc.targetedEntity instanceof PlayerEntity player)) return;

        String name = player.getGameProfile().getName();

        if (Bllry.FRIEND_MANAGER.contains(name)) {
            Bllry.FRIEND_MANAGER.remove(name);
            Bllry.CHAT_MANAGER.tagged("Successfully removed " + ChatUtils.getPrimary() + name + ChatUtils.getSecondary() + " from your friends list.", "MCF", getName());
        } else {
            Bllry.FRIEND_MANAGER.add(name);
            Bllry.CHAT_MANAGER.tagged("Successfully added " + ChatUtils.getPrimary() + name + ChatUtils.getSecondary() + " to your friends list.", "MCF", getName());
        }
    }
}
