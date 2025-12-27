package in.gopi.billingsoftware.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import in.gopi.billingsoftware.service.FileUploadService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final String baseDir = "uploads/";
    private final String serverUrl = "https://billing-backend-zvtd.onrender.com"; // Backend URL

    @Override
    public String addFile(MultipartFile file, String moduleName) throws IOException {
        if (file == null || file.isEmpty() || moduleName == null || moduleName.isEmpty())
            return null;

        // Create module folder if not exists (like uploads/categories/ or uploads/items/)
        String uploadDir = baseDir + moduleName + "/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Generate unique file name
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());

        // Return full URL for frontend including context path
        return serverUrl + "/api/v1.0/uploads/" + moduleName + "/" + fileName;
    }

    @Override
    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) return false;

        try {
            // Extract relative path from full URL
            // e.g. http://localhost:8080/api/v1.0/uploads/categories/123.png -> uploads/categories/123.png
            String relativePath = filePath.substring(filePath.indexOf("/uploads/") + 1);
            File file = new File(relativePath);
            return file.exists() && file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
