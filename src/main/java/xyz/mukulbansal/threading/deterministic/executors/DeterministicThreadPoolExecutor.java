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
 * An implementation of Executor Service for deterministic delegation of tasks to the threads.
 *
 * @author mukulbansal
 */
public class DeterministicThreadPoolExecutor<T extends DeterministicThread> implements ExecutorService {

    private int threadCount;
    private Thread[] threads;
    private boolean[] threadActiveStatus;
    private List<BlockingQueue<T>> queues;
    private boolean[] goingToReadFromQueue;
    private AtomicBoolean shutDownRequested;

    public DeterministicThreadPoolExecutor(int threadCount, int queueSizePerThread) {
        this(threadCount);
        for (int i = 0; i < this.threadCount; i++) {
            queues.add(i, new LinkedBlockingQueue<>(queueSizePerThread));
            startExecution(i);
        }
    }

    public DeterministicThreadPoolExecutor(int threadCount) {
        this.threadCount = threadCount;
        this.threads = new Thread[threadCount];
        this.goingToReadFromQueue = new boolean[threadCount];
        this.threadActiveStatus = new boolean[threadCount];
        shutDownRequested = new AtomicBoolean(Boolean.FALSE);
        this.queues = new LinkedList<>();
        for (int i = 0; i < this.threadCount; i++) {
            queues.add(i, new LinkedBlockingQueue<>());
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
                T task = null;
                try {
                    this.goingToReadFromQueue[i] = true;
                    task = queues.get(i).take();
                    this.goingToReadFromQueue[i] = false;
                } catch (InterruptedException e) {
                    // Ignoring interrupted exception because shutdown consumers
                    // interrupts waiting threads. This is a valid behavior.
                }
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

        // Waiting till all queues get empty i.e. every last task in the queue is picked by a thread
        int numberOfEmptyQueues = 0;
        while(numberOfEmptyQueues < this.threads.length) {
            for (int i = 0; i < this.threads.length; i++) {
                if (this.queues.get(i).isEmpty() && this.threadActiveStatus[i]) {
                    this.threadActiveStatus[i] = Boolean.FALSE;
                    numberOfEmptyQueues++;
                }
            }
        }

        // Interrupting thread waiting on their respective queues
        int numberOfThreadsStopped = 0;
        while(numberOfThreadsStopped < this.threads.length) {
            for (int i = 0; i < this.threads.length; i++) {
                if(!this.threads[i].isAlive()){
                    numberOfThreadsStopped++;
                } else if (this.threads[i].isAlive() && this.goingToReadFromQueue[i]) {
                    this.threads[i].interrupt();
                    numberOfThreadsStopped++;
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
    public void execute(T command) {
        if (shutDownRequested.get()) {
            throw new SecurityException(Constants.Mesages.SECURITY_MESSAGE);
        }
        int hash = command.hashCode() % threadCount;
        try {
            queues.get(hash).put(command);
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

    @Override
    @Deprecated
    public void execute(Runnable command) {
        if (command instanceof DeterministicThread) {
            execute((T) command);
            return;
        }
        throw new UnsupportedOperationException(Constants.Mesages.INVALID_TASK);
    }
}
