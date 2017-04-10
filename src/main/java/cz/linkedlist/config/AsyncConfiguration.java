package cz.linkedlist.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;


/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    private static final int POOL_SIZE = 2;
    private static final int QUEUE_CAPACITY = 20;
    private static final int MAX_POOL_SIZE = 4;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler sch = new ThreadPoolTaskScheduler();
        sch.setPoolSize(POOL_SIZE);
        return sch;
    }

    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor e = new ThreadPoolTaskExecutor();
        e.setCorePoolSize(POOL_SIZE);
        e.setQueueCapacity(QUEUE_CAPACITY);
        e.setMaxPoolSize(MAX_POOL_SIZE);
        return e;
    }

    @Bean
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
