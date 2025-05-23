package me.lily.bllry.commands;

import lombok.Getter;
import lombok.Setter;
import me.lily.bllry.Bllry;
import me.lily.bllry.utils.IMinecraft;

import java.util.Arrays;
import java.util.List;

@Getter @Setter
public abstract class Command implements IMinecraft {
    private String name, tag;
    private final String description, syntax;
    private final List<String> aliases;

    public Command() {
        RegisterCommand annotation = getClass().getAnnotation(RegisterCommand.class);

        name = annotation.name();
        tag = annotation.tag().isEmpty() ? annotation.name() : annotation.tag();
        description = annotation.description();
        syntax = annotation.syntax();
        aliases = Arrays.asList(annotation.aliases());
    }

    public abstract void execute(String[] args);

    public void messageSyntax() {
        Bllry.CHAT_MANAGER.info(name + " " + syntax);
    }
}
