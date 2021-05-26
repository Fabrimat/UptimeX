package me.fabrimat.uptimex.job.step;

import me.fabrimat.uptimex.AppServer;
import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.command.Command;
import me.fabrimat.uptimex.job.command.CommandManager;
import me.fabrimat.uptimex.job.command.exceptions.CommandException;

public class Step {
    private final Job job;
    private final Command command;
    private final Command fallbackCommand;
    private final boolean conditional;
    
    public Step(Job job, Command command, Command fallbackCommand, boolean conditional) {
        this.job = job;
        this.command = command;
        this.fallbackCommand = fallbackCommand;
        this.conditional = conditional;
    }
    
    public boolean execute() {
        CommandManager commandManager = AppServer.getInstance().getCommandManager();
        boolean success;
        
        try {
            success = commandManager.executeCommand(getJob(), this, getCommand());
        } catch (CommandException ex) {
            success = false;
        }
        
        if (!success) {
            try {
                commandManager.executeCommand(getJob(), this, getFallbackCommand());
            } catch (CommandException ignored) {
            }
        }
        
        return success;
    }
    
    public Job getJob() {
        return job;
    }
    
    public Command getCommand() {
        return command;
    }
    
    public Command getFallbackCommand() {
        return fallbackCommand;
    }
    
    public boolean isConditional() {
        return conditional;
    }
    
}
