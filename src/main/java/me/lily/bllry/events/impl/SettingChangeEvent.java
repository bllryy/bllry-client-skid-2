package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;
import me.lily.bllry.settings.Setting;

@Getter @AllArgsConstructor
public class SettingChangeEvent extends Event {
    private final Setting setting;
}
