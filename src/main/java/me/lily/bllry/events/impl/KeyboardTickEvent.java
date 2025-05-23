package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.lily.bllry.events.Event;

@AllArgsConstructor @Getter @Setter
public class KeyboardTickEvent extends Event {
    private float movementForward;
    private float movementSideways;
}
