package com.levibostian.androidblanky.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepoVo {

    public String full_name;
    public String description;

}
