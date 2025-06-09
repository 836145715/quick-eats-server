package com.zmx.common.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.user")
public class JwtUserProperties extends BaseJwtProperties{
}
