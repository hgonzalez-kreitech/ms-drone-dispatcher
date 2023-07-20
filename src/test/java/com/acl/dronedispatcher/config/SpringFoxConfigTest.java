package com.acl.dronedispatcher.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
class SpringFoxConfigTest {

    private SpringFoxConfig springFoxConfig;

    @BeforeEach
    void setUp() {
        SwaggerProperties swaggerProperties = new SwaggerProperties();
        swaggerProperties.setTitle("swagger");
        swaggerProperties.setDescription("Test API Description");
        swaggerProperties.setVersion("1.0");
        swaggerProperties.setTermsOfService("Test Terms of Service");
        swaggerProperties.setContactName("Test Contact");
        swaggerProperties.setLicenseUrl("Test License URL");

        springFoxConfig = new SpringFoxConfig(swaggerProperties);
    }

    @Test
    void testApiBean() {
        Docket docket = springFoxConfig.api();

        Assertions.assertNotNull(docket);

        Assertions.assertEquals("swagger", docket.getDocumentationType().getName());
        Assertions.assertEquals("2.0", docket.getDocumentationType().getVersion());
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        public SpringFoxConfig springFoxConfig(SwaggerProperties configs) {
            return new SpringFoxConfig(configs);
        }

        @Bean
        public SwaggerProperties swaggerProperties() {
            return new SwaggerProperties();
        }
    }
}