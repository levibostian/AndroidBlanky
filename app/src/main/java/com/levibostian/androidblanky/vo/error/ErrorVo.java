package com.levibostian.androidblanky.vo.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorVo {

    public String param;
    public String msg;
    public String value;

}
