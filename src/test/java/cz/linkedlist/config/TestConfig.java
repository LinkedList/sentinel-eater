package cz.linkedlist.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.*;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@SpringBootApplication(exclude = {
        ContextCredentialsAutoConfiguration.class,
        ContextInstanceDataAutoConfiguration.class,
        ContextRegionProviderAutoConfiguration.class,
        ContextResourceLoaderAutoConfiguration.class,
        ContextStackAutoConfiguration.class
})
public class TestConfig {
}
