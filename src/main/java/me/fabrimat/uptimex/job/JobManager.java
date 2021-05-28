package me.fabrimat.uptimex.job;

import me.fabrimat.uptimex.AppServer;
import me.fabrimat.uptimex.job.exceptions.JobAlreadyRegisteredException;
import me.fabrimat.uptimex.job.exceptions.JobNotRegisteredException;
import me.fabrimat.uptimex.scheduler.ScheduledTask;
import me.fabrimat.uptimex.scheduler.Scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JobManager {
    
    private final Map<String, Job> registeredJobs;
    
    public JobManager() {
        this.registeredJobs = new HashMap<>();
    }
    
    public void registerJob(Job job) {
        if (!getRegisteredJobs().containsKey(job.getJobName())) {
            getRegisteredJobs().put(job.getJobName(), job);
            scheduleJob(job);
        } else {
            throw new JobAlreadyRegisteredException("Job " + job.getJobName() + " already registered.");
        }
    }
    
    public void unregisterJob(String jobName) {
        Job job = getJob(jobName);
        if (job != null) {
            getRegisteredJobs().remove(jobName);
            cancelJob(job);
        } else {
            throw new JobNotRegisteredException("Job " + jobName + " not registered.");
        }
    }
    
    public void unregisterAllJobs() {
        for (Job job : getRegisteredJobs().values()) {
            unregisterJob(job.getJobName());
        }
    }
    
    private void scheduleJob(Job job) {
        if (job.getScheduleInfo() == null) {
            return;
        }
        
        Long delay = job.getScheduleInfo().getDelay();
        Long period = job.getScheduleInfo().getPeriod();
        TimeUnit unit = job.getScheduleInfo().getUnit();
        
        Scheduler scheduler = AppServer.getInstance().getScheduler();
        
        ScheduledTask scheduledTask = null;
        if (delay != null && period != null) {
            scheduledTask = scheduler.scheduleTask(job, delay, period, unit);
        } else if (period != null) {
            scheduledTask = scheduler.scheduleTask(job, 0, period, unit);
        } else if (delay != null) {
            scheduledTask = scheduler.scheduleTask(job, delay, unit);
        } else {
            scheduler.runTask(job);
        }
        
        job.getScheduleInfo().setScheduledTask(scheduledTask);
    }
    
    private void cancelJob(Job job) {
        if (job.getScheduleInfo() != null) {
            job.getScheduleInfo().getScheduledTask().cancel();
            job.getScheduleInfo().setScheduledTask(null);
        }
    }
    
    public Job getJob(String jobName) {
        return getRegisteredJobs().get(jobName);
    }
    
    public List<Job> getJobs() {
        return this.getRegisteredJobs().values().stream().toList();
    }
    
    protected Map<String, Job> getRegisteredJobs() {
        return registeredJobs;
    }
}
