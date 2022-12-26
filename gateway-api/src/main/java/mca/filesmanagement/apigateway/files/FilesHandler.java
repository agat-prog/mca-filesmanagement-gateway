package mca.filesmanagement.apigateway.files;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import mca.filesmanagement.apigateway.RequestUtils;
import mca.filesmanagement.apigateway.messaging.DocumentInfo;
import mca.filesmanagement.apigateway.messaging.FileInfo;
import mca.filesmanagement.apigateway.messaging.PhaseInfo;
import reactor.core.publisher.Mono;

@Service
public class FilesHandler {
	
	@Value("${files.url}")
	private String filesUrl;
	
	@Autowired
	private GatewayFileService gatewayFileService;
	
	@Autowired
	private GatewayBpmService gatewayBpmService;
	
	@Autowired
	private GatewayDocsService gatewayDocsService;
	
	@Autowired
	private GatewayUserService gatewayUserService;
	
	public FilesHandler() {
		super();
	}
	
	public Mono<ServerResponse> getFileDetail(ServerRequest request){
		String fileCode = request.pathVariable("fileCode");
		String token = RequestUtils.getToken(request);
		
		// Resultado que se devolverá y que se irá construyendo paso a paso
		FileInfo fileInfo = new FileInfo();
		/*
		 * En primer lugar se obtiene el expediente con todos sus datos de forma síncrona
		 */
		FileResponse fileResponse = this.gatewayFileService.getFileById(token, fileCode);
		this.update(fileInfo, fileResponse);

		/*
		 * Una vez obtenido el expediente, se puede acceder al resto de información: documentación, procesos, etc.
		 */
		List<Mono<?>> listOfMonos = new ArrayList<>();
		listOfMonos.add(this.gatewayBpmService.getProcessByCode(token, fileInfo.getProcesCode()));
		
		// Se obtiene más detalle del usuario
		listOfMonos.add(this.gatewayUserService.getUserByUserName(token, fileInfo.getUserName()));
		
		/*
		 * Aquí se va a buscar la información detallada de la documentación adjunta del expediente 
		 */
		fileInfo.getDocuments().forEach(documentInfo -> listOfMonos.add(this.gatewayDocsService.getDocumentByCode(token, documentInfo.getCode())));

		Mono<FileInfo> resultado = Mono.zip(listOfMonos, objectArray -> combinar(fileInfo, objectArray));
		return resultado.flatMap(maybe -> generateResponse(token, maybe));		
	}
	
	private Mono<ServerResponse> generateResponse(String token, FileInfo fileInfo){
		/*
		 * Antes de enviar el FileInfo hay que actualizar la información de usuarios
		 * que existen en las distintas fases.
		 */
		Set<String> listUsersNames = new HashSet<>();
		fileInfo.getPhases().stream().filter(p -> Objects.nonNull(p.getUser())).map(p -> p.getUser()).distinct().toList().forEach(user -> listUsersNames.add(user));
		fileInfo.getPhases().stream().filter(p -> Objects.nonNull(p.getUserFinished())).map(p -> p.getUserFinished()).distinct().toList().forEach(user -> listUsersNames.add(user));
		
		/*
		 * Para cada userName distinto encontrado, se realizan llamadas "reactivas" al servicio de usuarios
		 * y se asignan a la fases correspondientes.
		 */
		List<Mono<UserResponse>> listOfMonos = new ArrayList<>();
		listUsersNames.forEach(m -> listOfMonos.add(this.gatewayUserService.getUserByUserName(token, m)));
		return 	Mono.zip(listOfMonos, objectArray -> combinar(fileInfo, objectArray))
				.flatMap(maybe -> ServerResponse.ok()
				                .contentType(MediaType.APPLICATION_JSON)
				                .body(fromValue(maybe)));		
	}
	
	private FileInfo combinar(FileInfo fileInfo, Object[] objs) {
		for (Object obj : objs) {
			if (obj instanceof ProcessResponse) {
				this.update(fileInfo, (ProcessResponse) obj);
			}
			else if (obj instanceof DocumentResponse) {
				this.update(fileInfo, (DocumentResponse) obj);
			}
			else if (obj instanceof UserResponse) {
				this.update(fileInfo, (UserResponse) obj);
			}
		}
		return fileInfo;
	}
	
	private void update(FileInfo fileInfo, UserResponse userResponse) {
		String nameComplete =  new StringBuilder()
				.append(userResponse.getFirstName())
				.append(", ")
				.append(userResponse.getName())
				.toString();
		
		/*
		 * Nombre del usuario que creó el expediente
		 */
		if (fileInfo.getUserName().equalsIgnoreCase(userResponse.getUserName())) {
			fileInfo.setUserNameCompleted(nameComplete);	
		}
		
		/*
		 * Se asignan los nombres de usuarios en los documentos
		 */
		fileInfo.getDocuments().stream()
							.filter(d -> userResponse.getUserName().equalsIgnoreCase(d.getCreationUser()))
							.forEach(d -> d.setCreationUser(nameComplete));	
		fileInfo.getDocuments().stream()
							.filter(d -> userResponse.getUserName().equalsIgnoreCase(d.getUpdateUser()))
							.forEach(d -> d.setUpdateUser(nameComplete));
		/*
		 * Se asignan los nombres de usuarios en las fases del proceso asociado
		 */
		fileInfo.getPhases().stream()
							.filter(p -> userResponse.getUserName().equalsIgnoreCase(p.getUser()))
							.forEach(p -> p.setUserNameCompleted(nameComplete));
		fileInfo.getPhases().stream()
							.filter(p -> userResponse.getUserName().equalsIgnoreCase(p.getUserFinished()))
							.forEach(p -> p.setUserFinishedCompleted(nameComplete));
	}
	
	private void update(FileInfo fileInfo, ProcessResponse processResponse) {
		fileInfo.setDate(processResponse.getDate());
		processResponse.getPhases().forEach(p -> fileInfo.add(this.toPhaseInfo(p)));
	}
	
	private void update(FileInfo fileInfo, DocumentResponse response ) {
		DocumentInfo info = fileInfo.getDocuments().stream().filter(d -> d.getCode().equals(response.getCode())).findFirst().get();
		info.setActive(response.isActive());
		info.setCode(response.getCode());
		info.setCreationDate(response.getCreationDate());
		info.setCreationUser(response.getCreationUser());
		info.setName(response.getName());
		info.setUpdateDate(response.getUpdateDate());
		info.setUpdateUser(response.getUpdateUser());
		info.setUrl(response.getUrl());
	}
	
	private PhaseInfo toPhaseInfo(PhaseResponse phaseResponse) {
		PhaseInfo phaseInfo = new PhaseInfo();
		phaseInfo.setDate(phaseResponse.getDate());
		phaseInfo.setDateFinished(phaseResponse.getDateFinished());
		phaseInfo.setPhaseCode(phaseResponse.getPhaseCode());
		phaseInfo.setPhaseDescription(phaseResponse.getPhaseDescription());
		phaseInfo.setUser(phaseResponse.getUser());
		phaseInfo.setUserFinished(phaseResponse.getUserFinished());
		return phaseInfo;
	}
	
	private void update(FileInfo fileInfo, FileResponse fileResponse) {
		fileInfo.setCode(fileResponse.getCode());
		fileInfo.setDescription(fileResponse.getDescription());
		fileInfo.setId(fileResponse.getId());
		fileInfo.setInitOption(fileResponse.getInitOption());
		fileInfo.setPhaseCode(fileResponse.getPhaseCode());
		fileInfo.setProcesCode(fileResponse.getProcesCode());
		fileInfo.setUserName(fileResponse.getUserName());
		fileInfo.setFinished(fileResponse.isFinished());
		fileResponse.getDocuments().forEach(d -> fileInfo.add(new DocumentInfo(d)));
	}
}
