package com.cvc.financial.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {

    @Bean
    public Docket apiDocker() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cvc.financial.api"))
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, globalGetResponseMessage())
                .globalResponseMessage(RequestMethod.POST, globalPostPutResponseMessage())
                .globalResponseMessage(RequestMethod.PUT, globalPostPutResponseMessage())
                .globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessage())
                .apiInfo(apiInfo())
                .tags(  new Tag("Users", "Manage users"),
                        new Tag("Accounts", "Manage accounts"),
                        new Tag("Transfers", "Manage transfers"));
    }

    private List<ResponseMessage> globalGetResponseMessage() {
        return Arrays.asList(
                getResponseMessageNotFound(),
                getResponseMessageNotAcceptable(),
                getResponseMessageInternalServerError()
        );
    }

    private List<ResponseMessage> globalPostPutResponseMessage() {
        return Arrays.asList(
                getResponseMessageBadRequest(),
                getResponseMessageNotAcceptable(),
                getResponseMessageInternalServerError(),
                getResponseMessageUnsupportedMediaType()
        );
    }

    private List<ResponseMessage> globalDeleteResponseMessage() {
        return Arrays.asList(
                getResponseMessageBadRequest(),
                getResponseMessageInternalServerError()
        );
    }

    private ResponseMessage getResponseMessageBadRequest() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.BAD_REQUEST.value())
                .responseModel(new ModelRef("Problem"))
                .message("Invalid request (client error)")
                .build();
    }

    private ResponseMessage getResponseMessageNotFound() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.NOT_FOUND.value())
                .responseModel(new ModelRef("Problem"))
                .message("Resource not found")
                .build();
    }

    private ResponseMessage getResponseMessageNotAcceptable() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.NOT_ACCEPTABLE.value())
                .responseModel(new ModelRef("Problem"))
                .message("Resource has no representation that could be accepted by the consumer")
                .build();
    }

    private ResponseMessage getResponseMessageInternalServerError() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .responseModel(new ModelRef("Problem"))
                .message("Internal server error")
                .build();
    }

    private ResponseMessage getResponseMessageUnsupportedMediaType() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .responseModel(new ModelRef("Problem"))
                .message("Request refused because the body is in an unsupported format")
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CVCCorp API")
                .description("API for scheduling financial transfers")
                .version("1")
                .contact(new Contact("CVCCorp", "https://www.cvc.com", "contact@cvc.com"))
                .build();
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
