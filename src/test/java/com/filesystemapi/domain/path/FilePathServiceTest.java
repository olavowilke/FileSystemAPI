package com.filesystemapi.domain.path;

import com.filesystemapi.domain.path.dto.PathSaveGetResponse;
import com.filesystemapi.domain.path.validation.PathValidation;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static com.filesystemapi.configurations.CONSTConfig.UPLOAD_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilePathServiceTest {

    @InjectMocks
    private FilePathService filePathService;

    @Mock
    private PathValidation pathValidationMock;

    @Mock
    private FilePathRepository filePathRepositoryMock;

    private MockMultipartFile multipartFile;
    private FilePath filePath;
    private Path root;
    private String fullPath;
    private String filename;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        FileInputStream fis = new FileInputStream("./testFiles/Square_200x200.png");
        this.multipartFile = new MockMultipartFile("file", "123.jpg", null, fis);
        this.filename = "123.jpg";
        this.fullPath = UPLOAD_PATH + filename;
        this.filePath = new FilePath(UUID.randomUUID(), fullPath, filename);
        this.root = Paths.get(fullPath);
    }

    @Test
    public void saveFile() throws IOException {
        doCallRealMethod().when(pathValidationMock).validatePathExistsInFileSystem(fullPath, root);
        doCallRealMethod().when(pathValidationMock).validadePathExistsInDatabase(fullPath, filePath, filename);
        when(filePathRepositoryMock.findByPath(anyString())).thenReturn(filePath);
        when(filePathRepositoryMock.save(any(FilePath.class))).thenReturn(filePath);

        PathSaveGetResponse savedFile = filePathService.save(multipartFile);

        assertNotNull(savedFile);
        assertNotNull(savedFile.getFileByte());
        assertEquals(savedFile.getPath(), filePath.getPath());

        verify(pathValidationMock, times(1))
                .validatePathExistsInFileSystem(anyString(), any(Path.class));
        verify(pathValidationMock, times(1))
                .validadePathExistsInDatabase(anyString(), any(FilePath.class), anyString());
        verify(filePathRepositoryMock, times(1)).findByPath(anyString());
        verify(filePathRepositoryMock, times(1)).save(any(FilePath.class));
        verifyNoMoreInteractions(pathValidationMock);
        verifyNoMoreInteractions(filePathRepositoryMock);
    }

    @Test
    public void getFile() throws IOException {
        when(filePathRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(filePath));

        PathSaveGetResponse file = filePathService.getFile(UUID.randomUUID());

        assertNotNull(file);
        assertEquals(file.getPath(), filePath.getPath());

        verify(filePathRepositoryMock, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(filePathRepositoryMock);
    }

    @Test
    public void deleteFile() {
        when(filePathRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(filePath));
        doNothing().when(filePathRepositoryMock).delete(any(FilePath.class));

        filePathService.deleteFile(UUID.randomUUID());

        verify(filePathRepositoryMock, times(1)).findById(any(UUID.class));
        verify(filePathRepositoryMock, times(1)).delete(any(FilePath.class));
        verifyNoMoreInteractions(filePathRepositoryMock);
    }

}
