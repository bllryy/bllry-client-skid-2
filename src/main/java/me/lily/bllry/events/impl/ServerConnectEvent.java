package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

@AllArgsConstructor @Getter
public class ServerConnectEvent extends Event {
    private final ServerAddress address;
    private final ServerInfo info;
}
