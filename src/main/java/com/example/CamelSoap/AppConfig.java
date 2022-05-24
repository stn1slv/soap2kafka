package com.example.CamelSoap;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath*:cxf-config.xml"})
public class AppConfig {
}