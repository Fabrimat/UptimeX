package me.fabrimat.uptimex.job.command.exceptions;

public class CommandNotRegisteredException extends RuntimeException {
    public CommandNotRegisteredException(String message) {
        super(message);
    }
}
