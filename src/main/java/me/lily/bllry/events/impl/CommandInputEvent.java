package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;

@Getter @AllArgsConstructor
public class CommandInputEvent extends Event {
    private final String message;
}
