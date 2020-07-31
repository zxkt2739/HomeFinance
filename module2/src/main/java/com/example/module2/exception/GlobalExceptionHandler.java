package com.example.module2.exception;

import com.example.core.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error("global handle Exception", e);

        String code = "100400";
        String message = "operate unsuccessfully";

        return R.fail(code, message);
    }
}
