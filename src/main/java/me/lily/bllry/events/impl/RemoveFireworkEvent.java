package me.lily.bllry.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lily.bllry.events.Event;
import net.minecraft.entity.projectile.FireworkRocketEntity;

@Getter @AllArgsConstructor
public class RemoveFireworkEvent extends Event {
    private final FireworkRocketEntity entity;
}
