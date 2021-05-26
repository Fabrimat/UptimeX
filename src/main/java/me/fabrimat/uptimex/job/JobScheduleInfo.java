package me.fabrimat.uptimex.job;

import me.fabrimat.uptimex.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class JobScheduleInfo {
    private final Long delay;
    private final Long period;
    private final TimeUnit unit;
    private ScheduledTask scheduledTask;
    
    public JobScheduleInfo(Long delay, Long period, TimeUnit unit) {
        if (delay != null) {
            delay = Math.max(0, delay);
        }
        if (period != null) {
            period = Math.max(0, period);
        }
        
        this.delay = delay;
        this.period = period;
        this.unit = unit;
    }
    
    public Long getDelay() {
        return delay;
    }
    
    public Long getPeriod() {
        return period;
    }
    
    public TimeUnit getUnit() {
        return unit;
    }
    
    public ScheduledTask getScheduledTask() {
        return scheduledTask;
    }
    
    public void setScheduledTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }
    
}
