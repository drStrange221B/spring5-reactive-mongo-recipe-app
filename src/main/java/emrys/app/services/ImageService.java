package emrys.app.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService{


    public void saveImageFile(String id, MultipartFile file);

}
