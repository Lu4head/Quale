package br.com.quale.service;

import br.com.quale.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class FileValidationService {

    private static final long MAX_FILE_SIZE_MB = 50; // 50 MB
    private static final long MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif"
    );

    private static final List<String> FORBIDDEN_EXTENSIONS = Arrays.asList(
            ".exe", ".bat", ".cmd", ".sh", ".jar", ".war", ".dll"
    );

    public void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }

        validateFileSize(file);
        validateImageType(file);
        validateExtension(file);
    }

    public void validateDocumentFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }

        validateFileSize(file);
        validateExtension(file);
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ValidationException(
                    String.format("Arquivo muito grande. Tamanho máximo: %d MB", MAX_FILE_SIZE_MB)
            );
        }
    }

    private void validateImageType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new ValidationException(
                    "Tipo de arquivo não permitido. Use: JPEG, JPG, PNG ou GIF"
            );
        }
    }

    private void validateExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null) {
            String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
            if (FORBIDDEN_EXTENSIONS.contains(extension)) {
                throw new ValidationException("Extensão de arquivo não permitida: " + extension);
            }
        }
    }
}