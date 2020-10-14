package com.palmgo.com.cn.sharingdata.conf;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UrlPathHelper;


import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiResourceController;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2Controller;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Configuration
@EnableSwagger2
//@EnableApolloConfig
public class SwaggerConfig{

	public static final String DEFAULT_PATH = "/sharingdata";
	@Value("${common.swaggerHost}")
	private  String swaggerHost = "59.33.252.219";
	@Value("${common.licenseUrl}")
	private String licenseUrl = "http://"+swaggerHost;
	@Value("${spring.application.name}")
	private String appName = "appH5";
	@Value("${common.version}")
	private String version="1.0";
	
	/**
     * SwaggerUI资源访问
     *
     * @param servletContext
     * @param order
     * @return
     * @throws Exception
     */
    @Bean
    public SimpleUrlHandlerMapping swaggerUrlHandlerMapping(ServletContext servletContext,
                                                            @Value("${swagger.mapping.order:10}") int order) throws Exception {
        SimpleUrlHandlerMapping urlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, ResourceHttpRequestHandler> urlMap = new HashMap<>();
        {
            PathResourceResolver pathResourceResolver = new PathResourceResolver();
            pathResourceResolver.setAllowedLocations(new ClassPathResource("META-INF/resources/webjars/"));
            pathResourceResolver.setUrlPathHelper(new UrlPathHelper());
 
            ResourceHttpRequestHandler resourceHttpRequestHandler = new ResourceHttpRequestHandler();
            resourceHttpRequestHandler.setLocations(Arrays.asList(new ClassPathResource("META-INF/resources/webjars/")));
            resourceHttpRequestHandler.setResourceResolvers(Arrays.asList(pathResourceResolver));
            resourceHttpRequestHandler.setServletContext(servletContext);
            resourceHttpRequestHandler.afterPropertiesSet();
            //设置新的路径
            urlMap.put(DEFAULT_PATH + "/webjars/**", resourceHttpRequestHandler);
        }
        {
            PathResourceResolver pathResourceResolver = new PathResourceResolver();
            pathResourceResolver.setAllowedLocations(new ClassPathResource("META-INF/resources/"));
            pathResourceResolver.setUrlPathHelper(new UrlPathHelper());
 
            ResourceHttpRequestHandler resourceHttpRequestHandler = new ResourceHttpRequestHandler();
            resourceHttpRequestHandler.setLocations(Arrays.asList(new ClassPathResource("META-INF/resources/")));
            resourceHttpRequestHandler.setResourceResolvers(Arrays.asList(pathResourceResolver));
            resourceHttpRequestHandler.setServletContext(servletContext);
            resourceHttpRequestHandler.afterPropertiesSet();
            //设置新的路径
            urlMap.put(DEFAULT_PATH + "/**", resourceHttpRequestHandler);
        }
        urlHandlerMapping.setUrlMap(urlMap);
        //调整DispatcherServlet关于SimpleUrlHandlerMapping的排序
        urlHandlerMapping.setOrder(order);
        return urlHandlerMapping;
    }
    
    
    /**
     * SwaggerUI接口访问
     */
    @Controller
    @ApiIgnore
    @RequestMapping(DEFAULT_PATH)
    public  class SwaggerResourceController implements InitializingBean {
 
        @Autowired
        private ApiResourceController apiResourceController;
 
        @Autowired
        private Environment environment;
 
        @Autowired
        private DocumentationCache documentationCache;
 
        @Autowired
        private ServiceModelToSwagger2Mapper mapper;
 
        @Autowired
        private JsonSerializer jsonSerializer;
 
        private Swagger2Controller swagger2Controller;
 
        @Override
        public void afterPropertiesSet() {
            swagger2Controller = new Swagger2Controller(environment, documentationCache, mapper, jsonSerializer);
        }
 
        /**
         * 首页
         *
         * @return
         */
        @RequestMapping
        public ModelAndView index() {
            ModelAndView modelAndView = new ModelAndView("redirect:" + DEFAULT_PATH + "/swagger-ui.html");
            return modelAndView;
        }
 
        @RequestMapping("/swagger-resources/configuration/security")
        @ResponseBody
        public ResponseEntity<SecurityConfiguration> securityConfiguration() {
            return apiResourceController.securityConfiguration();
        }
 
        @RequestMapping("/swagger-resources/configuration/ui")
        @ResponseBody
        public ResponseEntity<UiConfiguration> uiConfiguration() {
            return apiResourceController.uiConfiguration();
        }
 
        @RequestMapping("/swagger-resources")
        @ResponseBody
        public ResponseEntity<List<SwaggerResource>> swaggerResources() {
            return apiResourceController.swaggerResources();
        }
 
        @RequestMapping(value = "/v2/api-docs", method = RequestMethod.GET, produces = {"application/json", "application/hal+json"})
        @ResponseBody
        public ResponseEntity<Json> getDocumentation(
                @RequestParam(value = "group", required = false) String swaggerGroup,
                HttpServletRequest servletRequest) {
            return swagger2Controller.getDocumentation(swaggerGroup, servletRequest);
        }
    }
	
	@Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).host(this.swaggerHost).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    /**
     * 设置完成后进入SwaggerUI，右上角出现“Authorization”按钮，点击即可输入我们配置的认证参数。
     * 对于不需要输入参数的接口（上文所述的包含auth的接口），在未输入Authorization参数就可以访问。
     * 其他接口则将返回401错误。点击右上角“Authorization”按钮，输入配置的参数后即可通过认证访问。参数输入后全局有效，无需每个接口单独输入。
     * 通过Swagger2的securitySchemes配置全局参数：如下列代码所示，securitySchemes的ApiKey中增加一个名为“Authorization”，type为“header”的参数。
     * @return
     */
    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    /**
     * 在Swagger2的securityContexts中通过正则表达式，设置需要使用参数的接口（或者说，是去除掉不需要使用认证参数的接口），
     * 如下列代码所示，通过PathSelectors.regex("^(?!auth).*$")，所有包含"auth"的接口不需要使用securitySchemes。
     * 即不需要使用上文中设置的名为“Authorization”，type为“header”的认证参数。
     * 通俗讲，就是能匹配上的就使用默认认证，就不使用header里面的Authorization认证参数
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build());
        return securityContexts;
    }


    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = {authorizationScope};
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }
	
	//用来创建该Api的基本信息（这些基本信息会展现在文档页面中）
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(this.appName)
                .description(this.licenseUrl)
                .termsOfServiceUrl(this.licenseUrl)
                .licenseUrl(this.licenseUrl)
                .version(this.version)
                .build();
    }
	
	
}
