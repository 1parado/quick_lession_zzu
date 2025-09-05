package com.ly.handler;

import com.ly.exception.BusinessException;
import com.ly.exception.SmsException;
import com.ly.result.R;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 处理系统异常
    @ExceptionHandler(Exception.class)
    //使用 @ExceptionHandler 注解来捕获并处理控制器层（Controller）抛出的异常
    public R<?> handleException(Exception e) {
        logger.error("系统异常: ", e);
        return R.error(500, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public R<?> handlerBusinessException(BusinessException e) {
        logger.error(e.getMessage() +  e.getCode());
        return R.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(SmsException.class)
    public R<?> handlerSmsException(SmsException e) {
        logger.error(e.getMessage() +  e.getCode());
        return R.error(e.getCode(),e.getMessage());
    }

}
