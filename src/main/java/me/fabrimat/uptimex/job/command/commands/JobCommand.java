package me.fabrimat.uptimex.job.command.commands;

import me.fabrimat.uptimex.AppServer;
import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.JobManager;
import me.fabrimat.uptimex.job.command.CommandExecutor;
import me.fabrimat.uptimex.job.command.commands.exceptions.IllegalCommandArgumentsException;
import me.fabrimat.uptimex.job.command.commands.exceptions.JobNotFoundException;
import me.fabrimat.uptimex.job.command.commands.exceptions.RecursiveJobException;
import me.fabrimat.uptimex.job.step.Step;

import java.util.Arrays;

public class JobCommand implements CommandExecutor {
    @Override
    public boolean execute(Job job, Step step, String[] args) throws IllegalCommandArgumentsException, JobNotFoundException, RecursiveJobException {
        if (args != null && args.length > 0) {
            String arg = String.join(" ", args);
            JobManager jobManager = AppServer.getInstance().getJobManager();
            Job callJob = jobManager.getJob(arg);
            if (callJob != null) {
                int count = 0;
                for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                    if (element.getClassName().equals(getClass().getName())) {
                        count++;
                    }
                    if (count > AppServer.getInstance().getMainConfig().getNestedJobsProtection()) {
                        throw new RecursiveJobException("Too many recursive jobs: " + count);
                    }
                }
                return callJob.runSteps();
            } else {
                throw new JobNotFoundException("Job " + arg + " not found");
            }
        } else {
            throw new IllegalCommandArgumentsException("Illegal command argument for command JOB: " + Arrays.toString(args));
        }
    }
    
    @Override
    public String getCommand() {
        return "JOB";
    }
}
