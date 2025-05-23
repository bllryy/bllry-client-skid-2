package me.lily.bllry.modules.impl.visuals;

import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.PlayerDeathEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.ModeSetting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;

@RegisterModule(name = "DeathEffects", description = "Renders certain effects on players when they die.", category = Module.Category.VISUALS)
public class DeathEffectsModule extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "The effect that will be rendered.", "Lightning", new String[]{"Lightning"});

    @SubscribeEvent
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (mode.getValue().equals("Lightning")) {
            LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, mc.world);

            entity.setPosition(event.getPlayer().getPos());
            entity.setId(-701);

            mc.world.addEntity(entity);
        }
    }
}
