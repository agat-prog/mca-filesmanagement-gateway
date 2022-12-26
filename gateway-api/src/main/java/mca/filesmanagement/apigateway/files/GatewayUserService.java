package mca.filesmanagement.apigateway.files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class GatewayUserService {

	@Value("${users.url}")
	private String userssUrl;
	
	private WebClient webClient;

	public GatewayUserService(WebClient webClient) {
		super();

		this.webClient = webClient;
	}

	@SuppressWarnings("deprecation")
	public Mono<UserResponse> getUserByUserName(String token, String userName) {
		Mono<ClientResponse> response = webClient.get()
				.uri(this.userssUrl + "/users/" + userName)
				.headers(h -> h.setBearerAuth(token)).exchange();
		return response.flatMap(resp -> {
			switch (resp.statusCode()) {
			case OK:
				return resp.bodyToMono(UserResponse.class);
			default:
				return Mono.error(new Exception("Unknown: " + resp.statusCode()));
			}
		});
	}
}
