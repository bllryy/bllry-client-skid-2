package me.lily.bllry.modules.impl.core;

import lombok.Getter;
import me.lily.bllry.Bllry;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import net.minecraft.util.Identifier;

@Getter
@RegisterModule(name = "Capes", description = "Applies the Bllry cape to yourself and to other users.", category = Module.Category.CORE, toggled = true, drawn = false)
public class CapesModule extends Module {
    public CapesModule() {
        this.capeTexture = Identifier.of(Bllry.MOD_ID, "textures/cape.png");
    }

    private final Identifier capeTexture;
}
