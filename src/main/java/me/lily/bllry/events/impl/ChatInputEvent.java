package me.lily.bllry.events.impl;

import lombok.*;
import me.lily.bllry.events.Event;

@Getter @Setter @AllArgsConstructor
public class ChatInputEvent extends Event {
    private String message;
}
