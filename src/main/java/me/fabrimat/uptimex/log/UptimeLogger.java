package me.fabrimat.uptimex.log;

import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class UptimeLogger extends Logger {
    private final LogDispatcher dispatcher = new LogDispatcher(this);
    
    public UptimeLogger(String loggerName, String filePattern, ConsoleReader reader) {
        super(loggerName, null);
        setLevel(Level.ALL);
        
        try {
            FileHandler fileHandler = new FileHandler(filePattern, 1 << 24, 8, true);
            fileHandler.setFormatter(new ConsoleFormatter(false));
            addHandler(fileHandler);
            
            LoggerHandler consoleHandler = new LoggerHandler(reader);
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new ConsoleFormatter(true));
            addHandler(consoleHandler);
        } catch (IOException ex) {
            System.err.println("Could not register logger!");
            ex.printStackTrace();
        }
        
        getDispatcher().start();
    }
    
    @Override
    public void log(LogRecord record) {
        getDispatcher().queue(record);
    }
    
    void doLog(LogRecord record) {
        super.log(record);
    }
    
    public LogDispatcher getDispatcher() {
        return this.dispatcher;
    }
}
