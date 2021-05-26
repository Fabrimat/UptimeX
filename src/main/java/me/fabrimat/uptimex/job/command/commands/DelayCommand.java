package me.fabrimat.uptimex.job.command.commands;

import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.command.CommandExecutor;
import me.fabrimat.uptimex.job.command.commands.exceptions.IllegalCommandArgumentsException;
import me.fabrimat.uptimex.job.step.Step;

import java.util.Arrays;

public class DelayCommand implements CommandExecutor {
    @Override
    public boolean execute(Job job, Step step, String[] args) throws IllegalCommandArgumentsException {
        if (args != null && args.length == 1) {
            int delay;
            try {
                delay = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                return false;
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                return false;
            }
        } else {
            throw new IllegalCommandArgumentsException("Illegal command argument for command DELAY: " + Arrays.toString(args));
        }
        return true;
    }
    
    @Override
    public String getCommand() {
        return "DELAY";
    }
}
