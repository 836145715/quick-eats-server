package com.zmx.common.properties;

import lombok.Data;

@Data
public class BaseJwtProperties {
    private long expire;

    private String header;

    private String tokenPrefix;

    private String secret;
}
