package xyz.mukulbansal.threading.deterministic.executors;

/**
 * @author mukulbansal
 */
public class DeterministicExecutors {

    public static DeterministicThreadPoolExecutor newDeterministicFixedThreadPool(int nThreads) {
        return new DeterministicThreadPoolExecutor(nThreads);
    }

    public static DeterministicThreadPoolExecutor newDeterministicFixedThreadPool(int nThreads, int queueSizePerThread) {
        return new DeterministicThreadPoolExecutor(nThreads, queueSizePerThread);
    }
}
