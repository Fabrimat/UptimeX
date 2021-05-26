package me.fabrimat.uptimex.job.command.commands.exceptions;

import me.fabrimat.uptimex.job.command.exceptions.CommandException;

public class IllegalCommandArgumentsException extends CommandException {
    public IllegalCommandArgumentsException(String message) {
        super(message);
    }
}
