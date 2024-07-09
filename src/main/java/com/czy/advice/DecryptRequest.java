package com.czy.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.czy.annotaticon.Decrypt;
import com.czy.config.KeyService;
import com.czy.threadLocal.AesKeyThreadLocal;
import com.czy.utils.AES;
import com.czy.utils.RSAUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author: czy
 * @date: 2024/7/9 上午10:17
 */
@ControllerAdvice
@ConditionalOnClass(KeyService.class)
public class DecryptRequest extends RequestBodyAdviceAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DecryptRequest.class);

    @Autowired(required = false)
    private KeyService keyService;


    /**
     * 该方法用于判断当前请求，是否要执行beforeBodyRead方法
     *
     * @param methodParameter handler方法的参数对象
     * @param targetType      handler方法的参数类型
     * @param converterType   将会使用到的Http消息转换器类类型
     * @return 返回true则会执行beforeBodyRead
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (null == keyService) {
            return false;
        }
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        InputStream inputStream = inputMessage.getBody();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String requestBody = result.toString(StandardCharsets.UTF_8.name());
        if (StringUtils.isBlank(requestBody)) {
            return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
        }
        try {
            JSONObject jsonObject = JSON.parseObject(requestBody);
            String rsaPrivateKey = keyService.getRsaPrivateKey();
            String content = jsonObject.getString(keyService.getContentJsonKey());
            String aesKey = jsonObject.getString(keyService.getAesJsonKey());

            aesKey = new String(RSAUtil.decryptByPrivateKey(RSAUtil.toBytes(aesKey), rsaPrivateKey, 256), StandardCharsets.UTF_8);
            String data = AES.decryptFromBase64(content, aesKey);

            jsonObject = JSON.parseObject(data);
            AesKeyThreadLocal.put(aesKey);
            final byte[] decryptedBody = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return new ByteArrayInputStream(decryptedBody);
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            logger.error("Decryption error", e);
            throw new IOException("Decryption error", e);
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
