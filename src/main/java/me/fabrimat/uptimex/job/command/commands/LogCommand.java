package me.fabrimat.uptimex.job.command.commands;

import me.fabrimat.uptimex.AppServer;
import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.command.CommandExecutor;
import me.fabrimat.uptimex.job.step.Step;

public class LogCommand implements CommandExecutor {
    @Override
    public boolean execute(Job job, Step step, String[] args) {
        AppServer.getInstance().getLogger().info(String.join(" ", args));
        return true;
    }
    
    @Override
    public String getCommand() {
        return "LOG";
    }
}
