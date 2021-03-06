package xyz.mukulbansal.threading.deterministic.exceptions;

/**
 * @author mukulbansal
 */
public class InterruptedRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -2021989937699520657L;

    /**
     * Constructs an <code>InterruptedRuntimeException</code> with no detail  message.
     */
    public InterruptedRuntimeException() {
        super();
    }

    /**
     * Constructs an <code>InterruptedRuntimeException</code> with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public InterruptedRuntimeException(String s) {
        super(s);
    }

    public InterruptedRuntimeException(Throwable cause) {
        super(cause);
    }
}
