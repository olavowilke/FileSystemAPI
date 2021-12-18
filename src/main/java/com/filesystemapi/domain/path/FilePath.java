package com.filesystemapi.domain.path;


import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.filesystemapi.configurations.CONSTConfig.SLASH;

@Entity
@Getter
public class FilePath {

    @Id
    private UUID id;

    private String path;
    private String filename;

    public FilePath() {
        this.id = UUID.randomUUID();
    }

    public FilePath(String path, String filename) {
        this();
        this.path = path;
        this.filename = filename;
    }

    public FilePath(UUID id, String path, String filename) {
        this.id = id;
        this.path = path;
        this.filename = filename;
    }

    public Path getFullPath() {
        return Paths.get(this.path + SLASH + this.filename);
    }

}
