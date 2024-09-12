package com.afpa.hebergement.util;

import org.junit.jupiter.api.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileUtilTest {

    private static final String FILES_UPLOAD_DIRECTORY = "Documents-Center-Upload";

    @BeforeEach
    void setUp() throws IOException {
        // Crée le répertoire de test avant chaque test
        Path uploadPath = Paths.get(FILES_UPLOAD_DIRECTORY);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Supprime le répertoire de test après chaque test
        Path uploadPath = Paths.get(FILES_UPLOAD_DIRECTORY);
        if (Files.exists(uploadPath)) {
            Files.walk(uploadPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
            Files.deleteIfExists(uploadPath);
        }
    }

    @Test
    void testSaveFile() throws IOException {
        // Mock du fichier MultipartFile
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("Test content".getBytes()));

        // Appel de la méthode saveFile
        String fileName = "test.txt";
        String fileCode = FileUtil.saveFile(fileName, multipartFile);

        // Vérification du fichier sauvegardé
        Path uploadPath = Paths.get(FILES_UPLOAD_DIRECTORY);
        Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
        assertTrue(Files.exists(filePath));
    }

    @Test
    void testUpdateFile() throws IOException {
        // Préparation du fichier existant
        MultipartFile initialFile = mock(MultipartFile.class);
        when(initialFile.getInputStream()).thenReturn(new ByteArrayInputStream("Initial content".getBytes()));
        String fileName = "test.txt";
        String fileCode = FileUtil.saveFile(fileName, initialFile);

        // Mock du nouveau fichier MultipartFile
        MultipartFile newFile = mock(MultipartFile.class);
        when(newFile.getInputStream()).thenReturn(new ByteArrayInputStream("Updated content".getBytes()));

        // Appel de la méthode updateFile
        String newFileName = "updated_test.txt";
        FileUtil.updateFile(fileName, newFileName, newFile, fileCode);

        // Vérification du fichier mis à jour
        Path uploadPath = Paths.get(FILES_UPLOAD_DIRECTORY);
        Path newFilePath = uploadPath.resolve(fileCode + "-" + newFileName);
        assertTrue(Files.exists(newFilePath));
    }

    @Test
    void testGetFileAsResource() throws IOException {
        // Préparation du fichier existant
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("Test content".getBytes()));
        String fileName = "test.txt";
        String fileCode = FileUtil.saveFile(fileName, multipartFile);

        // Appel de la méthode getFileAsResource
        Resource resource = FileUtil.getFileAsResource(fileCode);

        // Vérification de la ressource
        assertNotNull(resource);
        assertTrue(resource.exists());
    }

    @Test
    void testDeleteFile() throws IOException {
        // Préparation du fichier existant
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("Test content".getBytes()));
        String fileName = "test.txt";
        String fileCode = FileUtil.saveFile(fileName, multipartFile);

        // Appel de la méthode deleteFile
        FileUtil.deleteFile(fileCode);

        // Vérification de la suppression du fichier
        Path uploadPath = Paths.get(FILES_UPLOAD_DIRECTORY);
        Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
        assertFalse(Files.exists(filePath));
    }
}
