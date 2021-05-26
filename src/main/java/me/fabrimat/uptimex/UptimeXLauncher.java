package me.fabrimat.uptimex;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import me.fabrimat.uptimex.job.command.Command;

import java.util.Collections;

public class UptimeXLauncher {
    
    public static void main(String[] args) throws Exception {
        UptimeX uptimeX = new UptimeX();
        AppServer.setInstance(uptimeX);
        uptimeX.getLogger().info("Enabling " + uptimeX.getName() + " version " + uptimeX.getVersion());
        
        uptimeX.start();
        
        OptionParser parser = new OptionParser();
        parser.acceptsAll(Collections.singletonList("noconsole"), "Disable console input");
        OptionSet options = parser.parse(args);
        
        if (!options.has("noconsole")) {
            String line;
            while (uptimeX.isRunning() && (line = uptimeX.getConsoleReader().readLine(">")) != null) {
                if (!uptimeX.getCommandManager().dispatchCommand(new Command(line))) {
                    uptimeX.getLogger().warning("Command not found");
                }
            }
        }
    }
    
}
