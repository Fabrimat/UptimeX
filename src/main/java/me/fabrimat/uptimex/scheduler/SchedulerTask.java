package me.fabrimat.uptimex.scheduler;

import java.util.concurrent.Future;

public class SchedulerTask implements ScheduledTask {
    
    private final Scheduler scheduler;
    private final int id;
    private final Future<?> future;
    
    public SchedulerTask(Scheduler scheduler, int id, Future<?> future) {
        this.scheduler = scheduler;
        this.id = id;
        this.future = future;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public void cancel() {
        getScheduler().cancel(id);
    }
    
    @Override
    public boolean isActive() {
        return !getFuture().isCancelled() && !getFuture().isDone();
    }
    
    public Future<?> getFuture() {
        return future;
    }
    
    private Scheduler getScheduler() {
        return scheduler;
    }
}
