package me.fabrimat.uptimex.log;

import me.fabrimat.uptimex.log.utils.ConsoleColors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {
    
    private final DateFormat date = new SimpleDateFormat("HH:mm:ss");
    private final boolean coloured;
    
    public ConsoleFormatter(boolean coloured) {
        this.coloured = coloured;
    }
    
    @Override
    public String format(LogRecord record) {
        StringBuilder formatted = new StringBuilder();
        
        formatted.append(date.format(record.getMillis()));
        formatted.append(" [");
        appendLevel(formatted, record.getLevel());
        formatted.append("] ");
        formatted.append(formatMessage(record));
        formatted.append('\n');
        
        if (record.getThrown() != null) {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            formatted.append(writer);
        }
        
        return formatted.toString();
    }
    
    private void appendLevel(StringBuilder builder, Level level) {
        if (!coloured) {
            builder.append(level.getLocalizedName());
            return;
        }
        
        String color;
        
        if (level == Level.INFO) {
            color = ConsoleColors.ANSI_BLUE;
        } else if (level == Level.WARNING) {
            color = ConsoleColors.ANSI_YELLOW;
        } else if (level == Level.SEVERE) {
            color = ConsoleColors.ANSI_RED;
        } else {
            color = ConsoleColors.ANSI_CYAN;
        }
        
        builder.append(color).append(level.getLocalizedName()).append(ConsoleColors.ANSI_RESET);
    }
}
