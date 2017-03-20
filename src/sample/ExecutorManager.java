package sample;

/**
 * Created by kifkif on 13/03/2017.
 */

        import java.util.concurrent.Executor;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import java.util.concurrent.ThreadFactory;

public class ExecutorManager implements Executor
{
    private static ExecutorManager instance;

    public static ExecutorManager getInstance()
    {
        if(instance == null)
            instance = new ExecutorManager(4);

        return instance;
    }

    private ExecutorService executor;

    private ExecutorManager(int x)
    {
        this.executor = Executors.newFixedThreadPool(x, new DeamonFactory());
    }

    @Override
    public void execute(Runnable runnable)
    {
        executor.execute(runnable);
    }

    class DeamonFactory implements ThreadFactory
    {
        @Override
        public Thread newThread(Runnable runnable)
        {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        }

    }
}

