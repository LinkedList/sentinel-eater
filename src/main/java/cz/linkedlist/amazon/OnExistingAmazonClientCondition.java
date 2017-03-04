package cz.linkedlist.amazon;

import org.springframework.cloud.aws.core.config.AmazonWebserviceClientConfigurationUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
public class OnExistingAmazonClientCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnExistingAmazonClient.class.getName(), true);

        for (Object amazonClientClass : attributes.get("value")) {
            if (isAmazonClientMissing(context, (String) amazonClientClass)) {
                return false;
            }
        }

        return true;
    }

    private boolean isAmazonClientMissing(ConditionContext context, String amazonClientClass) {
        String amazonClientBeanName = AmazonWebserviceClientConfigurationUtils.getBeanName(amazonClientClass);
        return !context.getBeanFactory().containsBean(amazonClientBeanName) && !context.getRegistry().containsBeanDefinition(amazonClientBeanName);
    }
}
