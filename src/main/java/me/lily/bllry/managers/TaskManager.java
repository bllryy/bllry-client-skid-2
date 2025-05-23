package me.lily.bllry.managers;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.TickEvent;
import me.lily.bllry.utils.IMinecraft;

import java.util.ArrayList;

public class TaskManager implements IMinecraft {
    private final ArrayList<Runnable> tasks = new ArrayList<>();

    public TaskManager() {
        Bllry.EVENT_HANDLER.subscribe(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (!tasks.isEmpty()) {
            tasks.getFirst().run();
            tasks.removeFirst();
        }
    }

    public void submit(Runnable runnable) {
        tasks.add(runnable);
    }
}