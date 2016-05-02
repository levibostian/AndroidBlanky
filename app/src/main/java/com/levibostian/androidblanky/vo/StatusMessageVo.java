package com.levibostian.androidblanky.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusMessageVo {

    public String status;
    public String message;

}