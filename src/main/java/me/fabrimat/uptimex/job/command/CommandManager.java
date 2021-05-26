package me.fabrimat.uptimex.job.command;

import me.fabrimat.uptimex.AppServer;
import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.command.exceptions.CommandAlreadyRegisteredException;
import me.fabrimat.uptimex.job.command.exceptions.CommandException;
import me.fabrimat.uptimex.job.command.exceptions.CommandNotRegisteredException;
import me.fabrimat.uptimex.job.step.Step;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommandManager {
    
    private final Map<String, CommandExecutor> commands;
    
    public CommandManager() {
        commands = new HashMap<>();
    }
    
    public void registerCommand(CommandExecutor commandExecutor) {
        String commandName = commandExecutor.getCommand().toUpperCase(Locale.ROOT);
        if (!getCommands().containsKey(commandName)) {
            getCommands().put(commandName, commandExecutor);
            AppServer.getInstance().getLogger().info("Registered " + commandName + " command");
        } else {
            throw new CommandAlreadyRegisteredException("Command " + commandName + " already registered.");
        }
    }
    
    public void unregisterCommand(String commandName) {
        commandName = commandName.toUpperCase(Locale.ROOT);
        if (getCommands().containsKey(commandName)) {
            getCommands().remove(commandName);
            AppServer.getInstance().getLogger().info("Unregistered " + commandName + " command");
        } else {
            throw new CommandNotRegisteredException("Command " + commandName + " is not registered.");
        }
    }
    
    public CommandExecutor getCommandExecutor(String commandName) {
        commandName = commandName.toUpperCase(Locale.ROOT);
        return this.getCommands().get(commandName);
    }
    
    public boolean executeCommand(Job job, Step step, Command command) throws CommandException {
        if (command != null) {
            CommandExecutor executor = this.getCommandExecutor(command.getCommandName());
            if (executor != null) {
                return executor.execute(job, step, command.getArgs());
            } else {
                throw new CommandNotRegisteredException("Command " + command.getCommandName() + " is not registered.");
            }
        }
        return false;
    }
    
    public boolean dispatchCommand(Command command) {
        if (command != null) {
            if (this.getCommandExecutor(command.getCommandName()) == null) {
                return false;
            }
            AppServer.getInstance().getScheduler().runTask(() -> {
                boolean success;
                try {
                    success = executeCommand(null, null, command);
                } catch (CommandException ignored) {
                    success = false;
                }
                AppServer.getInstance().getLogger().info("Success: " + success);
            });
            return true;
        }
        return false;
    }
    
    protected Map<String, CommandExecutor> getCommands() {
        return commands;
    }
}
