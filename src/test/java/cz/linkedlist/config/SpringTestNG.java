package cz.linkedlist.config;

import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(classes = SpringTestNG.class, initializers = ConfigFileApplicationContextInitializer.class)
@ComponentScan(basePackages = "cz.linkedlist")
@TestPropertySource(locations="classpath:application.yml")
public class SpringTestNG extends AbstractTestNGSpringContextTests {
}
