package com.levibostian.androidblanky.vo.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldsMissingVo {

    public String status;
    public ErrorVo[] errors;

}