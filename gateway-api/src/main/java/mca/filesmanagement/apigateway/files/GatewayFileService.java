package mca.filesmanagement.apigateway.files;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GatewayFileService {

	@Value("${files.url}")
	private String filesUrl;
	
	private RestTemplate restTemplate = new RestTemplate();

	public GatewayFileService() {
		super();
	}

	public FileResponse getFileById(String token, String code) {
		String uriTemplate = this.filesUrl + "/files/{fileCode}";
		URI uri = UriComponentsBuilder.fromUriString(uriTemplate).build(code);

		RequestEntity<Void> requestEntity = RequestEntity.get(uri)
		        .header("Authorization", "Bearer " + token)
		        .build();

		ResponseEntity<FileResponse> response = restTemplate.exchange(requestEntity, FileResponse.class);
		
		return response.getBody();
	}
}
