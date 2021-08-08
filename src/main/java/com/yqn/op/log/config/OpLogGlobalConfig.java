package com.yqn.op.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huayuanlin
 * @date 2021/08/07 22:09
 * @desc the class desc
 */
@ConfigurationProperties(prefix = "op.log-global")
public class OpLogGlobalConfig {

    private String bizCode;


    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }
}
