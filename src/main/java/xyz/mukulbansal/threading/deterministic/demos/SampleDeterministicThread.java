package xyz.mukulbansal.threading.deterministic.demos;

import xyz.mukulbansal.threading.deterministic.threads.DeterministicThread;

/**
 * @author mukulbansal
 */
public class SampleDeterministicThread extends DeterministicThread {

    private int id;

    public SampleDeterministicThread(int id) {
        super();
        this.id = id;
    }

    @Override
    protected void runBusinessLogic() {
        System.out.println(String.format("Processsing id %s by thread %s", id, Thread.currentThread().getName()));
    }

    @Override
    public int hashCode() {
        return id;
    }
}
