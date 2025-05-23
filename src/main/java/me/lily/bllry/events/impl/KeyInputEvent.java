package me.lily.bllry.events.impl;

import lombok.*;
import me.lily.bllry.events.Event;

@EqualsAndHashCode(callSuper = true) @Data
public class KeyInputEvent extends Event {
    private final int key, modifiers;
}
