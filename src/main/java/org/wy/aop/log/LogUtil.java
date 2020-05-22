package org.wy.aop.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component(value = "LogUtil")
public class LogUtil {

    final Logger logger = LoggerFactory.getLogger(getClass());

    public void info(String str){
        logger.info(str);
    }

    public void warn(String str){
        logger.warn(str);
    }

    public void debug(String str){
        logger.debug(str);
    }

    public void error(String str){
        logger.error(str);
    }
}
