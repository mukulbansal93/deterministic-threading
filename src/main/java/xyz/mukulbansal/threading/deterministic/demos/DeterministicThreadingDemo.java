package xyz.mukulbansal.threading.deterministic.demos;

import xyz.mukulbansal.threading.deterministic.executors.DeterministicExecutors;
import xyz.mukulbansal.threading.deterministic.executors.DeterministicThreadPoolExecutor;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author mukulbansal
 */
public class DeterministicThreadingDemo {

    private static final Random RANDOM = new Random();
    private static final int THREADS = 5;

    public static void main(String[] args) {
        DeterministicThreadPoolExecutor deterministicThreadPoolExecutor = DeterministicExecutors.newDeterministicFixedThreadPool(THREADS);
        // Demostrating that tasks with same hash code will be executed by the same thread.
        IntStream.range(0, 100).forEach(value -> {
            deterministicThreadPoolExecutor.execute(new SampleDeterministicThread(RANDOM.nextInt(THREADS)) {
            });
        });
        // Demostrating that if all tasks have the same hash code, the execution becomes sequential and by only one thread.
        IntStream.range(0, 100).forEach(value -> {
            deterministicThreadPoolExecutor.execute(new SampleDeterministicThread(THREADS - 1) {
            });
        });
        deterministicThreadPoolExecutor.shutdown();
    }
}
