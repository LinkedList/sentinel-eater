package cz.linkedlist.amazon;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Conditional({OnExistingAmazonClientCondition.class})
public @interface ConditionalOnExistingAmazonClient {
    Class<?> value();
}
