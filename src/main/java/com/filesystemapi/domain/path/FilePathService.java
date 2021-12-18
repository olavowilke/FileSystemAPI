package com.filesystemapi.domain.path;

import com.filesystemapi.domain.path.dto.PathSaveGetResponse;
import com.filesystemapi.domain.path.validation.PathValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import static com.filesystemapi.configurations.CONSTConfig.FILE_NOT_FOUND;
import static com.filesystemapi.configurations.CONSTConfig.UPLOAD_PATH;

@Service
@RequiredArgsConstructor
public class FilePathService {

    private final PathValidation pathValidation;
    private final FilePathRepository filePathRepository;

    public PathSaveGetResponse save(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fullPath = UPLOAD_PATH + fileName;
        Path root = Paths.get(fullPath);
        byte[] photoByte = file.getBytes();

        pathValidation.validatePathExistsInFileSystem(fullPath, root);
        createFile(file, fileName, root);
        FilePath fileByPath = filePathRepository.findByPath(fullPath);
        FilePath validatedPath = pathValidation.validadePathExistsInDatabase(fullPath, fileByPath, fileName);
        filePathRepository.save(validatedPath);
        return new PathSaveGetResponse(validatedPath, photoByte);

    }

    private void createFile(MultipartFile file, String fileName, Path root) throws IOException {
        Files.copy(file.getInputStream(), root.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);
    }

    public PathSaveGetResponse getFile(UUID id) throws IOException {
        FilePath filePathById = getFilePathById(id);
        Path path = filePathById.getFullPath();
        byte[] file = Files.readAllBytes(path);
        return new PathSaveGetResponse(filePathById, file);
    }

    public void deleteFile(UUID id) {
        FilePath filePathById = getFilePathById(id);
        filePathRepository.delete(filePathById);
        String filePath = filePathById.getPath();
        FileSystemUtils.deleteRecursively(Paths.get(filePath)
                .toFile());
    }

    private FilePath getFilePathById(UUID id) {
        Optional<FilePath> filePathOptional = filePathRepository.findById(id);
        return filePathOptional.orElseThrow(() -> new EntityNotFoundException(FILE_NOT_FOUND));
    }

}
