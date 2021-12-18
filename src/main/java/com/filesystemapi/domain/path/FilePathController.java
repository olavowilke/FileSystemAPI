package com.filesystemapi.domain.path;

import com.filesystemapi.domain.path.dto.PathSaveGetResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/path")
public class FilePathController {

    private final FilePathService filePathService;

    @ApiOperation("Saves file in a File System and its PATH on the DB")
    @PostMapping()
    private PathSaveGetResponse saveFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return filePathService.save(multipartFile);
    }

    @ApiOperation("Retrieves a file from the File System")
    @GetMapping("/{id}")
    public PathSaveGetResponse getFile(@PathVariable("id") UUID id) throws IOException {
        return filePathService.getFile(id);
    }

    @ApiOperation("Deletes a file from the file system and its path from the DB")
    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable("id") UUID id) {
        filePathService.deleteFile(id);
    }

}
