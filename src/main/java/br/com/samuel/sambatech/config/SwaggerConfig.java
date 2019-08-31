package br.com.samuel.sambatech.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public ApiListingScannerPlugin pi1() {
    return new ApiListingScannerPlugin() {
      // constroi a tela de login no swagger
      @SuppressWarnings({"unchecked", "rawtypes"})
      @Override
      public List<ApiDescription> apply(DocumentationContext documentationContext) {
        return new ArrayList<ApiDescription>(
            Arrays.asList(new ApiDescription("/login", "Logar e pegar o JWT token",
                Arrays.asList(new OperationBuilder(new CachingOperationNameGenerator())
                    .authorizations(new ArrayList())
                    .notes("Log para pegar o token e usá-lo para acesso aos serviços da API")
                    .codegenMethodNameStem("login").method(HttpMethod.POST)
                    .parameters(Arrays.asList(new ParameterBuilder().description("Email / password")
                        .type(new TypeResolver().resolve(String.class))
                        .name("Formato JSON: {\n" + "	\"email\":\"\",\"password\":\"\"\n" + "}")
                        .parameterType("body").parameterAccess("access").required(true)
                        .modelRef(new ModelRef("string")).build()))
                    .build()),
                false)));
      }

      @Override
      public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
      }
    };
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
        .apis(RequestHandlerSelectors.basePackage("br.com.samuel.sambatech.controllers"))
        .paths(PathSelectors.any()).build().securitySchemes(Lists.newArrayList(apiKey()))
        .securityContexts(Lists.newArrayList(securityContext())).apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "API desenvolvida como o objetivo de concorrer a posição de desenvolvedor na SAMBATECH",
        "Esta API foi construída para teste para vaga de desenvolvedor na SAMBATECH", "Versão 1.0",
        "https://www.linkedin.com/in/samucapuc/",
        new Contact("Samuel Oliveira Chaves", StringUtils.EMPTY, "samucapuc@gmail.com"),
        "Não permitido a reprodução ou cópia sem permissão", StringUtils.EMPTY,
        Collections.emptyList());
  }

  @Bean
  SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any())
        .build();
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
  }

  private ApiKey apiKey() {
    return new ApiKey("JWT", "Authorization", "header");
  }

}
