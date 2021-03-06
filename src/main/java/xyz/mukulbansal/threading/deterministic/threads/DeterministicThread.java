package xyz.mukulbansal.threading.deterministic.threads;

/**
 * A wrapper over the {@link Thread} class which mandated the implementation of
 * methods needed for determinism.
 *
 * @author mukulbansal
 */
public abstract class DeterministicThread extends Thread {

    @Override
    public void run(){
        runBusinessLogic();
    }

    protected abstract void runBusinessLogic();

    public abstract int hashCode();
}
