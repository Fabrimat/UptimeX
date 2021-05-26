package me.fabrimat.uptimex.job.command.exceptions;

public class CommandAlreadyRegisteredException extends RuntimeException {
    public CommandAlreadyRegisteredException(String message) {
        super(message);
    }
}
