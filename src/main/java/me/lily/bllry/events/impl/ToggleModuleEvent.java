package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;
import me.lily.bllry.modules.Module;

@AllArgsConstructor @Getter
public class ToggleModuleEvent extends Event {
    private final Module module;
    private final boolean state;
}
