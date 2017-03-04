package cz.linkedlist.config;

import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ContextConfiguration(classes = TestConfig.class)
public @interface SentinelEaterTest {
}
