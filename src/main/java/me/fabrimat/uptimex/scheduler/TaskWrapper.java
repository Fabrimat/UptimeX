package me.fabrimat.uptimex.scheduler;

import me.fabrimat.uptimex.AppServer;

import java.util.logging.Level;

public class TaskWrapper implements Runnable {
    
    private final Runnable task;
    private final int id;
    
    TaskWrapper(Runnable task, int id) {
        this.task = task;
        this.id = id;
    }
    
    @Override
    public void run() {
        try {
            task.run();
        } catch (Throwable throwable) {
            AppServer.getInstance().getLogger().log(Level.SEVERE, String.format("Task %s encountered an exception", this), throwable);
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public Runnable getTask() {
        return task;
    }
    
    @Override
    public String toString() {
        return String.format("SchedulerTask(id=%d task=%s)", getId(), getTask());
    }
    
}
