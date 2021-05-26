package me.fabrimat.uptimex.job.command;

import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.command.exceptions.CommandException;
import me.fabrimat.uptimex.job.step.Step;

public interface CommandExecutor {
    boolean execute(Job job, Step step, String[] args) throws CommandException;
    
    String getCommand();
}
