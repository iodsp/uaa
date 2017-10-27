package org.iodsp.uaa.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fehash")
@Getter
@Setter
public class FeHashConfig {
    private String value = "";

    private String url = "";
}
