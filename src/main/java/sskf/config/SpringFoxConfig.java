package sskf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static com.google.common.collect.Lists.newArrayList;
@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("sskf.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
                .pathMapping("")
                .globalOperationParameters(
                        newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Authorization token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .build()));
    }
    @SuppressWarnings("deprecation")
    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "Spring Boot REST API",
                "Spring Boot REST API for A2SK1",
                "1.0",
                "Terms of service",
                "N/A", "N/A", "N/A");
        return apiInfo;
    }
}
