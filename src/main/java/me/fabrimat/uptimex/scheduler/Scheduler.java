package me.fabrimat.uptimex.scheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Scheduler {
    
    List<ScheduledTask> getActiveScheduledTasks();
    
    ScheduledTask runTask(Runnable task);
    
    ScheduledTask scheduleTask(Runnable task, long delay, TimeUnit unit);
    
    ScheduledTask scheduleTask(Runnable task, long delay, long period, TimeUnit unit);
    
    void cancel(int id);
    
    boolean cancelAll(long wait, TimeUnit unit);
    
    void cancelAllNow();
    
}
