package com.sil.jmongo.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Swagger 설정
 */

@Configuration
public class SwaggerConfig {

	@Value("${custom.server.host.api}") String HOST_API;

	private final ApplicationContext applicationContext;
	
	public SwaggerConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Bean
	public OpenAPI openAPI() {
		String jwt = "JWT";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
		
		Components components = new Components()
			// accessToken이라는 스키마 만들어주기
			.addSecuritySchemes(jwt, new SecurityScheme()
				.name("accessToken")
				.type(SecurityScheme.Type.APIKEY)
				.in(SecurityScheme.In.HEADER)
				.bearerFormat("JWT")
			);
		
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("JWT"))
				.info(apiInfo())
				.addSecurityItem(securityRequirement)
				.components(components)
				.servers(apiServer());
	}
	
	private Info apiInfo() {
		return new Info()
				.title("jmongo의 API")
				.version("0.0.0.0.0.0.0.1")
				.description("2025-06-27");
	}
	
	private List<Server> apiServer(){
		List<Server> server = new ArrayList<>();
		
		final Server localServer = new Server();
		localServer.setUrl(HOST_API);
		localServer.description("로컬 서버 도매인");
		server.add(localServer);

		final Server devServer = new Server();
		devServer.setUrl("http://192.168.10.1");
		devServer.description("개발 서버 도매인");
		server.add(devServer);
		
		return server;
	}
	
	@Bean
	public GroupedOpenApi publicApi() {
			return GroupedOpenApi.builder()
				.group("global")
				.pathsToMatch("/join", "/reIssue", "/global/**", "/api/**")
				.addOpenApiCustomizer(springSecurityLoginEndpointCustomizer(applicationContext))
				.build();
	}
	
	@Bean
	OpenApiCustomizer springSecurityLoginEndpointCustomizer(ApplicationContext applicationContext) {
		FilterChainProxy filterChainProxy = applicationContext.getBean(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, FilterChainProxy.class);
		return openAPI -> {
			for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {
				Optional<UsernamePasswordAuthenticationFilter> optionalFilter =
						filterChain.getFilters().stream()
								.filter(UsernamePasswordAuthenticationFilter.class::isInstance)
								.map(UsernamePasswordAuthenticationFilter.class::cast)
								.findAny();
				if (optionalFilter.isPresent()) {
					Operation operation = new Operation();
					Schema<?> schema = new ObjectSchema()
							.addProperty("username", new StringSchema()._default("admin"))
							.addProperty("password", new StringSchema()._default("1234"));
					RequestBody requestBody = new RequestBody().content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, new MediaType().schema(schema)));
					operation.requestBody(requestBody);
					ApiResponses apiResponses = new ApiResponses();
					apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()),
							new ApiResponse().description(HttpStatus.OK.getReasonPhrase())
									.content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
											new MediaType().example("{\"token\":\"sample-jwt-token\"}"))));

					apiResponses.addApiResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
							new ApiResponse().description(HttpStatus.UNAUTHORIZED.getReasonPhrase())
									.content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
											new MediaType().example("{\"error\":\"UNAUTHORIZED\"}"))));

					operation.responses(apiResponses);
					operation.addTagsItem("로그인");
					operation.summary("로그인");

					PathItem pathItem = new PathItem().post(operation);
					openAPI.getPaths().addPathItem("/login", pathItem);
				}
			}
		};
	}
}
