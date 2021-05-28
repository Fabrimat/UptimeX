package me.fabrimat.uptimex.scheduler;

import java.util.concurrent.ThreadFactory;

public class UptimeThreadFactory implements ThreadFactory {
    private int counter;
    
    public UptimeThreadFactory() {
        counter = 1;
    }
    
    @Override
    public Thread newThread(Runnable runnable) {
        Thread t = new Thread(runnable, "UptimeX Pool Thread #" + counter);
        counter++;
        return t;
    }
}
