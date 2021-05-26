package me.fabrimat.uptimex;

import jline.console.ConsoleReader;
import me.fabrimat.uptimex.config.JobConfig;
import me.fabrimat.uptimex.config.MainConfig;
import me.fabrimat.uptimex.job.Job;
import me.fabrimat.uptimex.job.JobManager;
import me.fabrimat.uptimex.job.command.CommandManager;
import me.fabrimat.uptimex.job.command.commands.*;
import me.fabrimat.uptimex.log.LoggingOutputStream;
import me.fabrimat.uptimex.log.UptimeLogger;
import me.fabrimat.uptimex.scheduler.Scheduler;
import me.fabrimat.uptimex.scheduler.UptimeScheduler;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UptimeX extends AppServer {
    
    private final CommandManager commandManager;
    private final JobManager jobManager;
    private final Logger logger;
    private final Scheduler scheduler;
    private final JobConfig jobConfig;
    private final MainConfig mainConfig;
    private final ReentrantLock shutdownLock = new ReentrantLock();
    private final ConsoleReader consoleReader;
    private volatile boolean isRunning = false;
    
    public UptimeX() throws IOException {
        this.consoleReader = new ConsoleReader();
        this.consoleReader.setExpandEvents(false);
        this.logger = new UptimeLogger("UptimeX", "uptimex.log", this.consoleReader);
        
        System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
    
        this.mainConfig = new MainConfig();
        this.jobConfig = new JobConfig();
        this.commandManager = new CommandManager();
        this.jobManager = new JobManager();
        this.scheduler = new UptimeScheduler(getMainConfig().getThreadPoolSize());
    }
    
    public static UptimeX getInstance() {
        return (UptimeX) AppServer.getInstance();
    }
    
    @Override
    public String getName() {
        return "UptimeX";
    }
    
    @Override
    public String getVersion() {
        return (UptimeX.class.getPackage().getImplementationVersion() == null) ?
                "unknown" : UptimeX.class.getPackage().getImplementationVersion();
    }
    
    @Override
    public void start() {
        getMainConfig().loadConfiguration();
        getJobConfig().loadConfiguration();
        getCommandManager().registerCommand(new DelayCommand());
        getCommandManager().registerCommand(new JobCommand());
        getCommandManager().registerCommand(new LogCommand());
        getCommandManager().registerCommand(new ShellCommand());
        getCommandManager().registerCommand(new ExitCommand());
        getCommandManager().registerCommand(new UrlCommand());
        
        setRunning(true);
        
        for (Job job : getJobConfig().getLoadedJobs()) {
            getJobManager().registerJob(job);
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> independentThreadStop(false)));
    }
    
    @Override
    public void stop() {
        new Thread("Shutdown Thread") {
            @Override
            public void run() {
                independentThreadStop(true);
            }
        }.start();
    }
    
    private void independentThreadStop(boolean callSystemExit) {
        // Acquire the shutdown lock
        // This needs to actually block here, otherwise running 'end' and then ctrl+c will cause the thread to terminate prematurely
        getShutdownLock().lock();
        
        // Acquired the shutdown lock
        if (!isRunning()) {
            // Server is already shutting down - nothing to do
            getShutdownLock().unlock();
            return;
        }
        setRunning(false);
        getLogger().info("Shutting down");
        
        getLogger().info("Unregistering jobs");
        getJobManager().unregisterAllJobs();
        
        getLogger().info("Closing scheduler");
        if (!getScheduler().cancelAll(5, TimeUnit.SECONDS)) {
            getLogger().info("Scheduler not responding. Terminating forcefully");
            getScheduler().cancelAllNow();
        }
        
        getLogger().info("Thank you and goodbye");
        
        if (getLogger() instanceof UptimeLogger uptimeLogger) {
            uptimeLogger.getDispatcher().interrupt();
            try {
                uptimeLogger.getDispatcher().join(1000);
            } catch (InterruptedException ignored) {
            }
        }
        
        // Need to close loggers after last message!
        for (Handler handler : getLogger().getHandlers()) {
            handler.close();
        }
        
        // Unlock the thread before optionally calling system exit, which might invoke this function again.
        // If that happens, the system will obtain the lock, and then see that isRunning == false and return without doing anything.
        getShutdownLock().unlock();
        
        if (callSystemExit) {
            System.exit(0);
        }
    }
    
    
    public synchronized boolean isRunning() {
        return isRunning;
    }
    
    private synchronized void setRunning(boolean running) {
        isRunning = running;
    }
    
    public ReentrantLock getShutdownLock() {
        return shutdownLock;
    }
    
    @Override
    public JobManager getJobManager() {
        return jobManager;
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
    
    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }
    
    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    @Override
    public JobConfig getJobConfig() {
        return jobConfig;
    }
    
    @Override
    public MainConfig getMainConfig() {
        return this.mainConfig;
    }
    
    public ConsoleReader getConsoleReader() {
        return consoleReader;
    }
    
}
