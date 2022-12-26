package mca.filesmanagement.apigateway.files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class GatewayDocsService {

	@Value("${docs.url}")
	private String docsUrl;
	
	private WebClient webClient;

	public GatewayDocsService(WebClient webClient) {
		super();

		this.webClient = webClient;
	}

	@SuppressWarnings("deprecation")
	public Mono<DocumentResponse> getDocumentByCode(String token, String code) {
		Mono<ClientResponse> response = webClient.get()
				.uri(this.docsUrl + "/docs/" + code)
				.headers(h -> h.setBearerAuth(token)).exchange();
		return response.flatMap(resp -> {
			switch (resp.statusCode()) {
			case OK:
				return resp.bodyToMono(DocumentResponse.class);
			default:
				return Mono.error(new Exception("Unknown: " + resp.statusCode()));
			}
		});
	}
}
