package xyz.mukulbansal.threading.deterministic.util;

/**
 * @author mukulbansal
 */
public class Constants {

    public interface Mesages {
        String SECURITY_MESSAGE = "Cannot accept tasks after shutdown request";
        String INVALID_TASK = "Can only accept tasks of type Deterministic Thread";
    }
}
