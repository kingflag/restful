package com.gordon.vc.restful.advice;


import com.alibaba.fastjson.JSONObject;
import com.gordon.vc.restful.request.UserInfoRequestHeader;
import com.gordon.vc.restful.util.BizException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Enumeration;

@Aspect
@Component
public class AuthorizeAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeAspect.class);

    @Pointcut("@annotation(com.gordon.vc.restful.advice.PreAuthorize)")
    public void permissionPointCut() {
    }

    @Around("permissionPointCut()")
    public Object permissionAround(ProceedingJoinPoint pjp) throws Throwable {
        UserInfoRequestHeader headerInfo = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if ("userInfoDecode".toLowerCase().equals(headerName)) {
                    String headerValueDecode = request.getHeader(headerName);
                    byte[] userInfoByte = Base64.getDecoder().decode(headerValueDecode);
                    String headerValue = new String(userInfoByte);
                    headerInfo = JSONObject.parseObject(headerValue, UserInfoRequestHeader.class);
                    if (headerInfo.getRole() == null) {
                        throw new BizException("未知权限");
                    }
                    break;
                }
            }
        }
        Object[] args = pjp.getArgs();
        String name = pjp.getSignature().getName();
        Signature signature = pjp.getSignature();
        MethodSignature msg = (MethodSignature) signature;
        Object target = pjp.getTarget();
        //获取注解标注的方法
        Method method = target.getClass().getMethod(msg.getName(), msg.getParameterTypes());
        //通过方法获取注解
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        Object proceed = null;
        try {
            String methodPermission = annotation.value();
            if (headerInfo != null) {
                if (headerInfo.getRole() == null) {
                    throw new BizException("非法请求");
                }
                if (!methodPermission.contains(headerInfo.getRole().toString())) {
                    throw new BizException("用户没有权限");
                }
            } else {
                throw new BizException("非法请求");
            }

            proceed = pjp.proceed(args);
        } catch (BizException bize) {
            throw new BizException(bize.getMsg());
        } catch (Exception e) {
            LOGGER.error("【环绕异常通知】【{}】方法出现异常，异常信息：", name, e);
            throw new BizException("出现未知异常");
        }
        return proceed;
    }
}
