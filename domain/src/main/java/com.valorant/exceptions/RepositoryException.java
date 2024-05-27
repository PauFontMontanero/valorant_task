package com.valorant.exceptions;

/**
 * Custom exception class to handle repository-related exceptions.
 * This class extends {@link RuntimeException} and provides various constructors
 * to create exceptions with custom messages and causes.
 */
public class RepositoryException extends RuntimeException {

    /**
     * Constructs a new {@code RepositoryException} with {@code null} as its detail message.
     */
    public RepositoryException() {
        super();
    }

    /**
     * Constructs a new {@code RepositoryException} with the specified cause and a detail message
     * of {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message
     * of {@code cause}). This constructor is useful for exceptions that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
     *              A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public RepositoryException(Throwable cause) {
        super(cause);
    }
}
