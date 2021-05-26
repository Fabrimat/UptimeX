package me.fabrimat.uptimex.job.command.commands.exceptions;

import me.fabrimat.uptimex.job.command.exceptions.CommandException;

public class RecursiveJobException extends CommandException {
    public RecursiveJobException(String message) {
        super(message);
    }
}
