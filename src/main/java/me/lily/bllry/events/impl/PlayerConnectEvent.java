package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;

import java.util.UUID;

@AllArgsConstructor @Getter
public class PlayerConnectEvent extends Event {
    private final UUID id;
}
