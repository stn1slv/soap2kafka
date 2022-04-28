package com.example.CamelSoap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties("adapter")
@Configuration
public class OpsMapping {
    private Map<String,String> opsMapping;

    public Map<String, String> getOpsMapping() {
        return opsMapping;
    }

    public void setOpsMapping(Map<String, String> opsMapping) {
        this.opsMapping = opsMapping;
    }
}
