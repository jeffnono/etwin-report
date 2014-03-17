package util;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:spring/spring.xml" })
public class BaseSpringTest extends AbstractJUnit4SpringContextTests{
}
