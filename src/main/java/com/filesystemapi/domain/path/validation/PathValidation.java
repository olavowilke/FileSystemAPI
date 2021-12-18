package com.filesystemapi.domain.path.validation;

import com.filesystemapi.domain.path.FilePath;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PathValidation {

    public void validatePathExistsInFileSystem(String path, Path root) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(Paths.get(path));
        }
    }

    public FilePath validadePathExistsInDatabase(String path, FilePath fileByPath, String filename) {
        return fileByPath == null
                ? newPath(path, filename)
                : fileByPath;
    }

    private FilePath newPath(String path, String filename) {
        return new FilePath(path, filename);
    }

}
