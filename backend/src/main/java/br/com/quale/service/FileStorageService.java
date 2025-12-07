package br.com.quale.service;

import br.com.quale.exception.FileStorageException;
import br.com.quale.exception.ResourceNotFoundException;
import br.com.quale.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${quale.upload-dir}") String uploadDir) {
        // Define o local de armazenamento dos arquivos com parâmetro passado no application.yml
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            log.warn("Erro ao criar o diretório de upload de arquivos: {}", ex.getMessage());
            throw new FileStorageException("Não foi possível criar o diretório para upload de arquivos." + ex);
        }
    }

    public String storeFile(MultipartFile file, String subfolder) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (originalFileName.contains("..")) {
                throw new ValidationException("Desculpe! O nome do arquivo contém uma sequência de caminho inválida " + originalFileName);
            }

            String fileExtension = "";
            try {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            } catch (Exception e) {
                log.warn("Erro ao obter a extensão do arquivo: {}", e.getMessage());
                throw new ValidationException("O arquivo deve ter uma extensão válida.");
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            Path targetLocation = this.fileStorageLocation.resolve(subfolder).resolve(uniqueFileName);
            Files.createDirectories(targetLocation.getParent()); // Garante que a subpasta existe
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            log.warn("Erro ao armazenar o arquivo {}: {}", originalFileName, ex.getMessage());
            throw new FileStorageException("Não foi possível armazenar o arquivo " + originalFileName + ". Por favor, tente novamente!" + ex);
        }
    }

    public void deleteFile(String fileName, String subfolder) {
        try {
            Path filePath = this.fileStorageLocation.resolve(subfolder).resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            log.warn("Erro ao deletar o arquivo {}: {}", fileName, ex.getMessage());
            throw new FileStorageException("Não foi possível deletar o arquivo " + fileName + ". Por favor, tente novamente!" + ex);
        }
    }

    public Resource getFile(String fileName, String subfolder) {
        try {
            Path filePath = this.fileStorageLocation.resolve(subfolder).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                log.warn("Arquivo não encontrado: {}", fileName);
                throw new ResourceNotFoundException("Arquivo não encontrado: " + fileName);
            }
        } catch (Exception ex) {
            log.warn("Erro ao carregar o arquivo {}: {}", fileName, ex.getMessage());
            throw new FileStorageException("Erro ao carregar o arquivo: " + fileName + ". Por favor, tente novamente!" + ex);
        }
    }
}