package cz.linkedlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Configuration
public class SchedulingConfiguration {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler sch = new ThreadPoolTaskScheduler();
        sch.setPoolSize(2);
        return sch;
    }
}
