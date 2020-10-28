package com.farhat.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Getter
@Setter
public class ConfigProps {

    @NestedConfigurationProperty
    private Clients clients = new Clients();

    @NestedConfigurationProperty
    private Index index = new Index();

}
