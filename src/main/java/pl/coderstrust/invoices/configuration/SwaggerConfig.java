package pl.coderstrust.invoices.configuration;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("pl.coderstrust.invoices"))
            .paths(PathSelectors.ant("/invoices/*"))
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            "REST Fakturownik",
            "Web application that allows manage invoices in easy way.",
            "1.0.0",
            "",
            new Contact("Fakturownik is created and maintained by Karolina Oliwa, Paweł Puchalski and Mariusz Reweda", "https://coderstrust.pl/", ""),
            "Fakturownik is available under the [MIT License].",
            "http://www.opensource.org/licenses/mit-license.php",
            Collections.emptyList());
    }

}
