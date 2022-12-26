package mca.filesmanagement.apigateway.files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class GatewayBpmService {

	@Value("${bpm.url}")
	private String bpmUrl;
	
	private WebClient webClient;

	public GatewayBpmService(WebClient webClient) {
		super();

		this.webClient = webClient;
	}

	@SuppressWarnings("deprecation")
	public Mono<ProcessResponse> getProcessByCode(String token, String code) {
		Mono<ClientResponse> response = webClient.get()
				.uri(this.bpmUrl + "/bpm/" + code)
				.headers(h -> h.setBearerAuth(token)).exchange();
		return response.flatMap(resp -> {
			switch (resp.statusCode()) {
			case OK:
				return resp.bodyToMono(ProcessResponse.class);
			default:
				return Mono.error(new Exception("Unknown: " + resp.statusCode()));
			}
		});
	}
}
