package com.czy.advice;

import com.alibaba.fastjson.JSONObject;
import com.czy.annotaticon.Encrypt;
import com.czy.config.KeyService;
import com.czy.pojo.AjaxResultVo;
import com.czy.threadLocal.AesKeyThreadLocal;
import com.czy.utils.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author: czy
 * @date: 2024/7/9 上午10:39
 */
@ControllerAdvice
@ConditionalOnClass(KeyService.class)
public class EncryptResponse implements ResponseBodyAdvice<Object> {

    @Autowired(required = false)
    private KeyService keyService;

    /**
     * 该方法用于判断当前请求的返回值，是否要执行beforeBodyWrite方法
     *
     * @param methodParameter handler方法的参数对象
     * @param converterType   将会使用到的Http消息转换器类类型
     * @return 返回true则会执行beforeBodyWrite
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        if (null == keyService) {
            return false;
        }
        return methodParameter.hasMethodAnnotation(Encrypt.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String aesKeyKey = AesKeyThreadLocal.get();
        if (body instanceof AjaxResultVo) {
            Object data = ((AjaxResultVo<?>) body).getData();
            if (null == data || "".equals(data)) {
                return body;
            } else if (data.getClass() == String.class) {
                //处理String
                body = data;
            } else if (isPrimitiveOrWrapper(data.getClass())) {
                //处理基本类型和包装类型
                body = data.toString();
            } else {
                //处理对象转成json
                body = JSONObject.toJSON(data);
            }
        }
        //把接口的出参，用前端此次传过来的AES密钥进行加密，然后返回
        String encryptData = AES.encryptToBase64(body.toString(), aesKeyKey);
        AesKeyThreadLocal.remove();
        body = AjaxResultVo.ok(encryptData);
        return body;
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == Boolean.class
                || clazz == Character.class
                || Number.class.isAssignableFrom(clazz)
                ;
    }
}
