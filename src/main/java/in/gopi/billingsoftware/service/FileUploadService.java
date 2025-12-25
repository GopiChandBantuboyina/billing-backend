package in.gopi.billingsoftware.service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileUploadService {
    String addFile(MultipartFile file,String moduleName) throws IOException;
    boolean deleteFile(String fileName);
}
