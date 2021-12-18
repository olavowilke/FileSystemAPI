package com.filesystemapi.domain.path.dto;

import com.filesystemapi.domain.path.FilePath;
import lombok.Getter;

@Getter
public class PathSaveGetResponse {

    private String id;
    private String path;
    private byte[] fileByte;

    public PathSaveGetResponse(FilePath validatedPath, byte[] fileByte) {
        this.id = validatedPath.getId().toString();
        this.path = validatedPath.getPath();
        this.fileByte = fileByte;
    }

}
