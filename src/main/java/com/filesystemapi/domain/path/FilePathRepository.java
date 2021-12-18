package com.filesystemapi.domain.path;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FilePathRepository extends JpaRepository<FilePath, UUID> {

    FilePath findByPath(String path);

}
