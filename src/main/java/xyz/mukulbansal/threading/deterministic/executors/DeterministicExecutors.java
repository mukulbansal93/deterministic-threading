package xyz.mukulbansal.threading.deterministic.executors;

/**
 * @author mukulbansal
 */
public class DeterministicExecutors {

    public static DeterministicThreadPoolExecutor newDeterministicFixedThreadPool(int nThreads) {
        return new DeterministicThreadPoolExecutor(nThreads);
    }
}
