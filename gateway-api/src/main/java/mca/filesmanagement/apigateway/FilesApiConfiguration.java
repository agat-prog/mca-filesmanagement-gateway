package mca.filesmanagement.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import mca.filesmanagement.apigateway.files.FilesHandler;

/**
 * Files API configuration.
 */
@Configuration
public class FilesApiConfiguration {

	@Value("${files.url}")
	private String filesUrl;

	@Value("${bpm.url}")
	private String bpmUrl;
	
	@Value("${index.url}")
	private String indexUrl;

	/**
	 * Products API route locator bean instance.
	 *
	 * @param builder route locator builder.
	 * @return route locator instance.
	 */
	@Bean
	public RouteLocator productProxyRouting(RouteLocatorBuilder builder) {
		return builder.routes()
					.route("files", r -> r.path("/api/files/**").uri(this.filesUrl))
					.route("bpm", r -> r.path("/api/bpm/**").uri(this.bpmUrl))
					.route("index", r -> r.path("/api/index/**").uri(this.indexUrl))
					.build();
	}
	
	@Bean
	public WebClient webClient() {
		return WebClient.create();
	}
	
	@Bean
	public RouterFunction<ServerResponse> filesHandleRouting (FilesHandler filesHandler){
		return RouterFunctions.route(RequestPredicates.GET("/api/files/{fileCode}"), filesHandler::getFileDetail);
	}
}
