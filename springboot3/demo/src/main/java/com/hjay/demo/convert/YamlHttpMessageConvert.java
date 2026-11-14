package com.hjay.demo.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 自定义实现消息转换器
 */
public class YamlHttpMessageConvert extends AbstractHttpMessageConverter<Object> {

    private ObjectMapper objectMapper;


    /**
     *   mvc:
     *       # 指定请求参数名代表需要协商的名 当前设置为 ?type=json，type后面拼类型
     *       parameter-name: type
     *       media-types:
     *         yaml: text/yaml
     */
    public YamlHttpMessageConvert() {
        // 设置可以转换的消息类型 Accept: text/yaml 格式 并添加 到适配器中
        super(new MediaType("text", "yaml", StandardCharsets.UTF_8));
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        objectMapper = new ObjectMapper(yamlFactory);
    }

    /**
     * 判断是否支持消息转换
     * 这里简化不做判断
     * @param clazz the class to test for support
     * @return
     */
    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * 根据 RequestBody 来判断是否支持 请求参数转换
     * @param clazz the type of object to return
     * @param inputMessage the HTTP input message to read from
     * @return
     * @throws IOException
     * @throws HttpMessageNotReadableException
     */
    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    /**
     *  根据 ResponseBody 来判断是否支持 响应结果转换
     * @param returnValue the object to write to the output message
     * @param outputMessage the HTTP output message to write to
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Object returnValue, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            this.objectMapper.writeValue(outputMessage.getBody(), returnValue);
        } finally {
            outputMessage.getBody().close();
        }
    }
}
