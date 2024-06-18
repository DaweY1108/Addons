package me.dawey.addons.commands;

public class CommandData {
    public String condition;

    public String trueCommand;

    public String falseCommand;

    public CommandData(String condition, String trueCommand, String falseCommand) {
        this.condition = condition;
        this.trueCommand = trueCommand;
        this.falseCommand = falseCommand;
    }

}
