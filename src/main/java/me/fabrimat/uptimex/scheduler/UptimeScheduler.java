package me.fabrimat.uptimex.scheduler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UptimeScheduler implements Scheduler {
    
    private final AtomicInteger taskCounter = new AtomicInteger();
    private final ScheduledExecutorService service;
    private final Map<Integer, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>();
    
    public UptimeScheduler(int threadPoolSize) {
        service = Executors.newScheduledThreadPool(threadPoolSize, new UptimeThreadFactory());
    }
    
    public ScheduledExecutorService getExecutorService() {
        return service;
    }
    
    @Override
    public ScheduledTask runTask(Runnable task) {
        return this.scheduleTask(task, 0, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public ScheduledTask scheduleTask(Runnable task, long delay, TimeUnit unit) {
        int id = getTaskCounter().getAndIncrement();
        Runnable taskWrapped = new TaskWrapper(task, id);
        Future<?> future = getExecutorService().schedule(taskWrapped, delay, unit);
        ScheduledTask scheduledTask = new SchedulerTask(this, id, future);
        getScheduledTasks().put(id, scheduledTask);
        return scheduledTask;
    }
    
    @Override
    public ScheduledTask scheduleTask(Runnable task, long delay, long period, TimeUnit unit) {
        int id = getTaskCounter().getAndIncrement();
        Runnable taskWrapped = new TaskWrapper(task, id);
        Future<?> future = getExecutorService().scheduleAtFixedRate(taskWrapped, delay, period, unit);
        ScheduledTask scheduledTask = new SchedulerTask(this, id, future);
        getScheduledTasks().put(id, scheduledTask);
        return scheduledTask;
    }
    
    @Override
    public void cancel(int id) {
        ScheduledTask task = getScheduledTasks().get(id);
        if (task != null) {
            task.getFuture().cancel(false);
            getScheduledTasks().remove(id);
        }
    }
    
    private Map<Integer, ScheduledTask> getScheduledTasks() {
        return scheduledTasks;
    }
    
    public List<ScheduledTask> getActiveScheduledTasks() {
        return getScheduledTasks().values().stream().toList();
    }
    
    private AtomicInteger getTaskCounter() {
        return this.taskCounter;
    }
    
    @Override
    public boolean cancelAll(long wait, TimeUnit unit) {
        for (ScheduledTask scheduledTask : getScheduledTasks().values()) {
            scheduledTask.cancel();
        }
        getScheduledTasks().clear();
        
        getExecutorService().shutdown();
        try {
            return getExecutorService().awaitTermination(wait, unit);
        } catch (InterruptedException ex) {
            return false;
        }
    }
    
    @Override
    public void cancelAllNow() {
        getExecutorService().shutdownNow();
        getScheduledTasks().clear();
    }
}
