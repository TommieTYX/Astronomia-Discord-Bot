package com.astronomia.core;

import lombok.Getter;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class CommandListener extends ListenerAdapter {

    private CommandData commandData;

    public String getCommand() {
        return commandData.getName();
    }

    public String getDescription() {
        return commandData.getDescription();
    }

    public List<OptionData> getArgs() {
        return commandData.getOptions();
    }

    protected void init(@Nonnull String command, @Nonnull String description) {
        commandData = new CommandData(command, description);
    }

    protected void addArgs(OptionType optionType, String arg, String description) {
        commandData.addOption(optionType, arg, description, false);
    }

    protected void addArgs(OptionType optionType, String arg, String description, boolean isRequired) {
        commandData.addOption(optionType, arg, description, isRequired);
    }
}