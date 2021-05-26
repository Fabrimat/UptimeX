package me.fabrimat.uptimex;

import me.fabrimat.uptimex.config.JobConfig;
import me.fabrimat.uptimex.config.MainConfig;
import me.fabrimat.uptimex.job.JobManager;
import me.fabrimat.uptimex.job.command.CommandManager;
import me.fabrimat.uptimex.scheduler.Scheduler;

import java.io.InputStream;
import java.util.logging.Logger;

public abstract class AppServer {
    
    private static AppServer instance;
    
    public static AppServer getInstance() {
        return AppServer.instance;
    }
    
    public static void setInstance(AppServer instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance must be non null");
        }
        if (AppServer.instance != null) {
            throw new IllegalStateException("Instance already set");
        }
        AppServer.instance = instance;
    }
    
    public abstract String getName();
    
    public abstract String getVersion();
    
    public abstract Logger getLogger();
    
    public abstract Scheduler getScheduler();
    
    public abstract JobManager getJobManager();
    
    public abstract JobConfig getJobConfig();
    
    public abstract MainConfig getMainConfig();
    
    public abstract CommandManager getCommandManager();
    
    public String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }
    
    public abstract void start();
    
    public abstract void stop();
    
    public final InputStream getResourceAsStream(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }
}
