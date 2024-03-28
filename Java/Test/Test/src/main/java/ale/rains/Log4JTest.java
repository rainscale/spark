package ale.rains;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class Log4JTest {
    private static final Logger logger = Logger.getLogger(Log4JTest.class);

    public static void main(String[] args) {
        logger.trace("This is trace message.");
        logger.debug("This is debug message.");
        logger.info("This is info message.");
        logger.warn("This is warn message.");
        logger.error("This is error message.");
        logger.fatal("This is fatal message.");
    }
}
