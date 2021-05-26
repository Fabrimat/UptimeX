package me.fabrimat.uptimex.job.command;

import java.util.Arrays;

public class Command {
    private final String commandName;
    private final String[] args;
    
    public Command(String command) {
        if (command != null) {
            String[] commandSplit = command.split(" ");
            this.commandName = commandSplit[0];
            if (commandSplit.length > 1) {
                args = Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
            } else {
                args = null;
            }
        } else {
            commandName = "";
            args = null;
        }
    }
    
    public String getCommandName() {
        return this.commandName;
    }
    
    public String[] getArgs() {
        return this.args;
    }
}
