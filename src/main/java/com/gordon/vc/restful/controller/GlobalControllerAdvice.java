package com.gordon.vc.restful.controller;


import com.gordon.vc.restful.response.PackResp;
import com.gordon.vc.restful.util.BizException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerAdvice {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * <1> 处理 form data方式调用接口校验失败抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public PackResp<String> bindExceptionHandler(BindException e) {
        LOGGER.error(e.getMessage(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());
        return PackResp.fail(collect.toString());
    }

    /**
     * <2> 处理 json 请求体调用接口校验失败抛出的异常
     *
     * @param httpServletResponse
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public PackResp<String> methodArgumentNotValidExceptionHandler(HttpServletResponse httpServletResponse, MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());
        return PackResp.fail(collect.toString());
    }

    /**
     * <3> 处理单个参数校验失败抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public PackResp<String> constraintViolationExceptionHandler(ConstraintViolationException e) {
        LOGGER.error(e.getMessage(), e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());
        return PackResp.fail(collect.toString());
    }


    /**
     * <4> 未知抛出的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public PackResp<String> handle(Exception e) {
        LOGGER.error(e.getMessage(), e);
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            return PackResp.fail(bizException.getMsg());
        } else {
            return PackResp.fail(e.getLocalizedMessage());
        }
    }
}




