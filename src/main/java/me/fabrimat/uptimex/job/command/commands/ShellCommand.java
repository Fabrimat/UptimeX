package me.fabrimat.uptimex.job.command.commands;

import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.command.CommandExecutor;
import me.fabrimat.uptimex.job.command.exceptions.CommandException;
import me.fabrimat.uptimex.job.step.Step;

import java.io.IOException;

public class ShellCommand implements CommandExecutor {
    @Override
    public boolean execute(Job job, Step step, String[] args) throws CommandException {
        Process pr;
        try {
            pr = Runtime.getRuntime().exec(String.join(" ", args));
        } catch (IOException e) {
            return false;
        }
        try {
            return pr.waitFor() == 0;
        } catch (InterruptedException e) {
            return false;
        }
    }
    
    @Override
    public String getCommand() {
        return "SHELL";
    }
}
