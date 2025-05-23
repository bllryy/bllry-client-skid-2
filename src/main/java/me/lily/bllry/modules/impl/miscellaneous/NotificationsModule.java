package me.lily.bllry.modules.impl.miscellaneous;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.ClientConnectEvent;
import me.lily.bllry.events.impl.PlayerDeathEvent;
import me.lily.bllry.events.impl.PlayerPopEvent;
import me.lily.bllry.events.impl.TickEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.utils.chat.ChatUtils;
import me.lily.bllry.utils.minecraft.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;

import java.util.ArrayList;

@RegisterModule(name = "Notifications", description = "Notifies you in chat whenever something significant happens.", category = Module.Category.MISCELLANEOUS)
public class NotificationsModule extends Module {
    public BooleanSetting totemPops = new BooleanSetting("TotemPops", "Notifies you in chat whenever a player pops a totem.", true);
    public BooleanSetting visualRange = new BooleanSetting("VisualRange", "Notifies you in chat whenever a player enters your render distance.", false);
    public BooleanSetting pearlThrows = new BooleanSetting("PearlThrows", "Notifies you in chat whenever a player throws a pearl.", true);

    private final ArrayList<PlayerEntity> loadedPlayers = new ArrayList<>();
    private final ArrayList<Integer> thrownPearls = new ArrayList<>();

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (getNull()) return;

        if (visualRange.getValue()) {
            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof PlayerEntity player) || entity == mc.player) continue;

                if (!loadedPlayers.contains(player)) {
                    loadedPlayers.add(player);
                    Bllry.CHAT_MANAGER.message(ChatUtils.getPrimary() + player.getName().getString() + ChatUtils.getSecondary() + " has entered your visual range.", "visual-range-" + player.getName().getString());
                }
            }

            if (!loadedPlayers.isEmpty()) {
                for (PlayerEntity player : new ArrayList<>(loadedPlayers)) {
                    if (!mc.world.getPlayers().contains(player)) {
                        loadedPlayers.remove(player);
                        Bllry.CHAT_MANAGER.message(ChatUtils.getPrimary() + player.getName().getString() + ChatUtils.getSecondary() + " has left your visual range.", "visual-range-" + player.getName().getString());
                    }
                }
            }
        }

        if (pearlThrows.getValue()) {
            for(Entity e : mc.world.getEntities()) {
                if(!(e instanceof EnderPearlEntity pearl)) continue;
                if(pearl.getOwner() == null || thrownPearls.contains(pearl.getId())) continue;

                String name = pearl.getOwner().getName().getString();
                Bllry.CHAT_MANAGER.message(ChatUtils.getPrimary() + name + ChatUtils.getSecondary() + " threw a pearl towards " + EntityUtils.getPearlDirection(pearl).toString() + ".", "pearl-throws-" + name);
                thrownPearls.add(pearl.getId());
            }

            thrownPearls.removeIf(id -> !(mc.world.getEntityById(id) instanceof EnderPearlEntity));
        }
    }

    @SubscribeEvent
    public void onClientConnect(ClientConnectEvent event) {
        loadedPlayers.clear();
    }

    @SubscribeEvent
    public void onPlayerPop(PlayerPopEvent event) {
        if (totemPops.getValue()) {
            Bllry.CHAT_MANAGER.message(ChatUtils.getPrimary() + event.getPlayer().getName().getString() + ChatUtils.getSecondary() + " has popped " + ChatUtils.getPrimary() + event.getPops() + ChatUtils.getSecondary() + " totem" + (event.getPops() > 1 ? "s" : "") + ".", "totem-pop-" + event.getPlayer().getName().getString());
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(PlayerDeathEvent event) {
        int pops = Bllry.WORLD_MANAGER.getPoppedTotems().getOrDefault(event.getPlayer().getUuid(), 0);
        if (totemPops.getValue() && pops > 0) {
            Bllry.CHAT_MANAGER.message(ChatUtils.getPrimary() + event.getPlayer().getName().getString() + ChatUtils.getSecondary() + " has died after popping " + ChatUtils.getPrimary() + pops + ChatUtils.getSecondary() + " totem" + (pops > 1 ? "s" : "") + ".", "totem-pop-" + event.getPlayer().getName().getString());
        }
    }
}
