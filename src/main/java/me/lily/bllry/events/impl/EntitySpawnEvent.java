package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;
import net.minecraft.entity.Entity;

@AllArgsConstructor @Getter
public class EntitySpawnEvent extends Event {
    private final Entity entity;
}
