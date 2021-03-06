package xyz.mukulbansal.threading.deterministic.executors;

import xyz.mukulbansal.threading.deterministic.exceptions.InterruptedRuntimeException;
import xyz.mukulbansal.threading.deterministic.threads.DeterministicThread;
import xyz.mukulbansal.threading.deterministic.util.Constants;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An implementation of Executor Service for determimistic delegation of tasks to the threads.
 *
 * @author mukulbansal
 */
public class DeterministicThreadPoolExecutor<T extends DeterministicThread> implements ExecutorService {

    private static final int CAPACITY = 10;

    private int threadCount;
    private Thread[] threads;
    private boolean[] threadActiveStatus;
    private List<BlockingQueue<T>> queues;
    private AtomicBoolean shutDownRequested;

    public DeterministicThreadPoolExecutor(int threadCount) {
        this.threadCount = threadCount;
        this.threads = new Thread[threadCount];
        this.threadActiveStatus = new boolean[threadCount];
        shutDownRequested = new AtomicBoolean(Boolean.FALSE);
        this.queues = new LinkedList<>();
        for (int i = 0; i < this.threadCount; i++) {
            queues.add(i, new LinkedBlockingDeque<>(CAPACITY));
            startExecution(i);
        }
    }

    /**
     * Start execution of all threads which will consumer from their respective Blocking Queues.
     *
     * @param i
     */
    private void startExecution(int i) {
        this.threadActiveStatus[i] = Boolean.TRUE;
        this.threads[i] = new Thread(() -> {
            while (this.threadActiveStatus[i]) {
                T task = queues.get(i).poll();
                if (null != task) {
                    task.run();
                }
            }
        });
        this.threads[i].start();
    }

    @Override
    public void shutdown() {
        this.shutDownRequested = new AtomicBoolean(Boolean.TRUE);
        boolean allStopped = Boolean.FALSE;
        while (!allStopped) {
            for (int i = 0; i < this.threads.length; i++) {
                if (this.queues.get(i).isEmpty()) {
                    this.threadActiveStatus[i] = Boolean.FALSE;
                }
            }
            allStopped = Boolean.TRUE;
            for (Thread thread : this.threads) {
                if (thread.isAlive()) {
                    allStopped = Boolean.FALSE;
                }
            }
        }
    }

    /**
     * This method will assign the tasks to threads in a deterministic way.
     * This method is blocking if trying to submit task to a queue which is filled till capacity.
     *
     * @param command
     */
    @Override
    public void execute(Runnable command) {
        if (shutDownRequested.get()) {
            throw new SecurityException(Constants.Mesages.SECURITY_MESSAGE);
        }
        int hash = command.hashCode() % threadCount;
        try {
            queues.get(hash).put((T) command);
        } catch (InterruptedException e) {
            throw new InterruptedRuntimeException(e);
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        // TODO
        return null;
    }

    @Override
    public boolean isShutdown() {
        return shutDownRequested.get();
    }

    @Override
    public boolean isTerminated() {
        // TODO
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        // TODO
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        // TODO
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        // TODO
        return null;
    }

    @Override
    public Future<?> submit(Runnable task) {
        // TODO
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        // TODO
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        // TODO
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        // TODO
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        // TODO
        return null;
    }
}
