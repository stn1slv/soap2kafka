package com.example.CamelSoap;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.camel.model.ChoiceDefinition;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.camel.component.cxf.CxfEndpoint;

import java.util.Map;

@Component
public class Routes extends RouteBuilder {
    private static final Logger log = LoggerFactory.getLogger(Routes.class);
    @Autowired
    private OpsMapping opsMapping;

    @Override
    public void configure() throws Exception {
        RouteDefinition rd=from("cxf://http://0.0.0.0:{{adapter.port}}?wsdlURL={{adapter.wsdlURL}}&dataFormat=PAYLOAD&properties.schema-validation-enabled=true").streamCaching()
                .log("${body}")
                .log("${header.operationName}")
                .log("${header.operationNamespace}");
        buildOpsMapping(rd);
        rd.to("kafka:input?brokers={{adapter.kafka}}");
        log.debug("RouteDefinition: "+rd.toString());
    }

    private void buildOpsMapping(RouteDefinition rd){
        Map<String, String> mapping=opsMapping.getOpsMapping();
        log.debug("opsMapping: "+ mapping);
        if (!mapping.isEmpty()){
            ChoiceDefinition cd =rd.choice();
            for (Map.Entry<String, String> entry : mapping.entrySet()) {
                cd.when(simple("${header.operationName} == '"+entry.getKey()+"'")).to("kafka:"+entry.getValue()+"?brokers={{adapter.kafka}}");
            }
            cd.otherwise().log("OtherWise!"); //Error raise exception
            cd.endChoice();
        }
    }
}
