package mca.filesmanagement.apigateway;

import java.util.Objects;

import org.springframework.web.reactive.function.server.ServerRequest;

public class RequestUtils {

	public RequestUtils() {
		super();
	}
	
	public static String getToken(ServerRequest request) {
		String token = request.headers().firstHeader("Authorization");
		if (Objects.nonNull(token)) {
			String[] split = token.split(" ");
			if (split.length > 1) {
				token = split[1].trim();
			}
		}
		return token;
	}
}
