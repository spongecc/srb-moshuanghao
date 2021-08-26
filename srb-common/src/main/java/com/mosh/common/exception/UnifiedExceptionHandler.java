package com.mosh.common.exception;

import com.mosh.common.result.R;
import com.mosh.common.result.ResponseEnum;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/25
 */
@Component
@RestControllerAdvice
public class UnifiedExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        return R.error().message(e.getMessage());
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public R handleBadSqlGrammarException(BadSqlGrammarException e){
        return R.result(ResponseEnum.BAD_SQL_GRAMMAR_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public R handleBusinessException(BusinessException e){
        return R.error().message(e.getMessage()).code(e.getCode());
    }

}
