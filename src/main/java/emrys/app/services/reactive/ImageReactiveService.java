package emrys.app.services.reactive;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface ImageReactiveService {

     Mono<Void> saveImageFile(String id, MultipartFile file);
}
