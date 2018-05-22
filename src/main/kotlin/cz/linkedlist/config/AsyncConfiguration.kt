package cz.linkedlist.config

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

import java.util.concurrent.Executor


/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>.
 */
@Configuration
@EnableAsync
class AsyncConfiguration {

    val asyncExecutor: Executor
        @Bean
        @Primary
        get() {
            val e = ThreadPoolTaskExecutor()
            e.corePoolSize = POOL_SIZE
            e.setQueueCapacity(QUEUE_CAPACITY)
            e.maxPoolSize = MAX_POOL_SIZE
            return e
        }

    val asyncUncaughtExceptionHandler: AsyncUncaughtExceptionHandler
        @Bean
        get() = SimpleAsyncUncaughtExceptionHandler()

    @Bean
    fun threadPoolTaskScheduler(): ThreadPoolTaskScheduler {
        val sch = ThreadPoolTaskScheduler()
        sch.poolSize = POOL_SIZE
        return sch
    }

    companion object {

        private val POOL_SIZE = 2
        private val QUEUE_CAPACITY = 20
        private val MAX_POOL_SIZE = 4
    }
}
