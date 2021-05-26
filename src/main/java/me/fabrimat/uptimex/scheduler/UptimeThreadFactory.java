package me.fabrimat.uptimex.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

public class UptimeThreadFactory implements ThreadFactory {
    private int counter;
    
    public UptimeThreadFactory() {
        counter = 1;
    }
    
    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        Thread t = new Thread(runnable, "UptimeX Pool Thread #" + counter);
        counter++;
        return t;
    }
}
