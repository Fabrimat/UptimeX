package me.fabrimat.uptimex.job.command.commands.exceptions;

import me.fabrimat.uptimex.job.command.exceptions.CommandException;

public class JobNotFoundException extends CommandException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
