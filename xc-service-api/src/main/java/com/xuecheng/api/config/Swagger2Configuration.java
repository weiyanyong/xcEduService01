package com.xuecheng.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//
@Configuration
//Swagger接口生成工作原理
//项目启动会扫描组件@ComponentScan(basePackages={"com.xuecheng.api"})，从而扫描到@EnableSwagger2注解标志的配置类
// 配置类中扫描com.xuecheng下@RestController标注的类（CmsPageController implements CmsPageControllerApi），
// 根据CmsPageController此类实现的接口CmsPageControllerApi中的swagger的注解（@Api、@ApiOperation）生成swagger接口
@EnableSwagger2
public class Swagger2Configuration {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //private ApiInfo apiInfo()
                .apiInfo(apiInfo())
                .select()
                //扫描包
                .apis(RequestHandlerSelectors.basePackage("com.xuecheng"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("学成网api文档")
                .description("学成网api文档")
//                .termsOfServiceUrl("/")
                .version("1.0")
                .build();
    }

}
