package me.fabrimat.uptimex.job.command.commands;

import me.fabrimat.uptimex.AppServer;
import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.command.CommandExecutor;
import me.fabrimat.uptimex.job.command.exceptions.CommandException;
import me.fabrimat.uptimex.job.step.Step;

public class ExitCommand implements CommandExecutor {
    @Override
    public boolean execute(Job job, Step step, String[] args) throws CommandException {
        AppServer.getInstance().stop();
        return true;
    }
    
    @Override
    public String getCommand() {
        return "EXIT";
    }
}
