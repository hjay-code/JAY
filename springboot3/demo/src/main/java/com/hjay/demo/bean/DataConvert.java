package com.hjay.demo.bean;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement // 添加xml 转换注解，自动转换
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataConvert {

    private long id;
    private String name;
}
