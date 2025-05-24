package me.lily.bllry.modules;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import lombok.Getter;
import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.KeyInputEvent;
import me.lily.bllry.events.impl.MouseInputEvent;
import me.lily.bllry.settings.Setting;
import me.lily.bllry.utils.IMinecraft;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Getter
public class ModuleManager implements IMinecraft {
    private final List<Module> modules = new ArrayList<>();
    private final Map<Class<? extends Module>, Module> moduleClasses = new Reference2ReferenceOpenHashMap<>();

    public ModuleManager() {
        Bllry.EVENT_HANDLER.subscribe(this);

        try {
            for (Class<?> clazz : new Reflections("me.lily.bllry.modules.impl").getSubTypesOf(Module.class)) {
                if (clazz.getAnnotation(RegisterModule.class) == null) continue;
                Module module = (Module) clazz.getDeclaredConstructor().newInstance();

                for (Field field : module.getClass().getDeclaredFields()) {
                    if (!Setting.class.isAssignableFrom(field.getType())) continue;
                    if (!field.canAccess(module)) field.setAccessible(true);

                    module.getSettings().add((Setting) field.get(module));
                }

                module.getSettings().add(module.chatNotify);
                module.getSettings().add(module.drawn);
                module.getSettings().add(module.bind);

                modules.add(module);
                moduleClasses.put(module.getClass(), module);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            Bllry.LOGGER.error("Failed to register the client's modules!", exception);
        }

        modules.sort(Comparator.comparing(Module::getName));
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        modules.stream()
                .filter(m -> m.getBind() == event.getKey())
                .forEach(m -> m.setToggled(!m.isToggled()));
    }

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) {
        modules.stream()
                .filter(m -> m.getBind() == (-event.getButton() - 1))
                .forEach(m -> m.setToggled(!m.isToggled()));
    }

    public List<Module> getModules(Module.Category category) {
        return modules.stream().filter(m -> m.getCategory() == category).toList();
    }

    public Module getModule(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) moduleClasses.get(clazz);
    }
}
