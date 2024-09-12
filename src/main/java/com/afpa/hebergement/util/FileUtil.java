package com.afpa.hebergement.util;

import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * Utilitaire pour la gestion des fichiers tels que l'enregistrement, la mise à jour, la récupération et la suppression de fichiers.
 * Cette classe fournit des méthodes pour gérer les fichiers stockés sur le serveur.
 */
public class FileUtil {

    private FileUtil() {}

    private static final String FILES_UPLOAD_DIRECTORY = "Documents-Center-Upload";

    /**
     * Enregistre un fichier téléchargé sur le serveur.
     *
     * @param fileName Le nom du fichier à enregistrer. Ce nom sera utilisé avec un code unique pour identifier le fichier.
     * @param multipartFile Le fichier téléchargé via MultipartFile, généralement provenant d'une requête HTTP.
     * @return Un code unique généré pour le fichier, qui est utilisé pour l'identifier de manière unique sur le serveur.
     * @throws IOException Si une erreur survient lors de l'enregistrement du fichier sur le serveur.
     *                     Cela peut être dû à des problèmes de permissions, des erreurs d'entrée/sortie, ou d'autres problèmes liés au fichier.
     */
    public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        // Définit le chemin d'enregistrement des fichiers en utilisant la constante FILES_UPLOAD_DIRECTORY.
        Path uploadPath = Paths.get(FILES_UPLOAD_DIRECTORY);

        // Crée le répertoire s'il n'existe pas encore.
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Génère un code unique pour le fichier en utilisant une chaîne alphanumérique de 8 caractères.
        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        // Enregistre le fichier sur le serveur en le copiant dans le répertoire spécifié avec le code unique ajouté au nom du fichier.
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file");
        }

        // Retourne le code unique du fichier généré, qui peut être utilisé pour la gestion ultérieure du fichier.
        return fileCode;
    }

    /**
     * Met à jour un fichier existant avec un nouveau fichier téléchargé.
     *
     * @param oldFileName Le nom du fichier à remplacer sur le serveur.
     * @param newFileName Le nom du nouveau fichier à enregistrer sur le serveur.
     * @param multipartFile Le nouveau fichier téléchargé via MultipartFile.
     * @param fileCode Le code unique associé au fichier que l'on souhaite remplacer.
     * @throws IOException Si une erreur survient lors de la mise à jour du fichier, ce qui peut inclure des problèmes de fichiers ou de répertoires.
     */
    public static void updateFile(String oldFileName, String newFileName, MultipartFile multipartFile, String fileCode) throws IOException {
        // Définit le chemin d'enregistrement des fichiers en utilisant la constante FILES_UPLOAD_DIRECTORY.
        Path uploadPath = Paths.get(FILES_UPLOAD_DIRECTORY);

        // Vérifie si le répertoire existe. Si ce n'est pas le cas, une exception est lancée.
        if (!Files.exists(uploadPath)) {
            throw new IOException("Upload directory does not exist");
        }

        // Génère les chemins complets pour le fichier à remplacer et le nouveau fichier.
        Path oldFilePath = uploadPath.resolve(fileCode + "-" + oldFileName);
        Path newFilePath = uploadPath.resolve(fileCode + "-" + newFileName);

        // Appelle la méthode pour remplacer le fichier en passant les chemins et le nouveau fichier.
        replaceFile(oldFilePath, newFilePath, multipartFile);
    }

    /**
     * Remplace un fichier existant par un nouveau fichier.
     *
     * @param oldFilePath Le chemin du fichier à remplacer.
     * @param newFilePath Le chemin du nouveau fichier à enregistrer.
     * @param multipartFile Le nouveau fichier téléchargé via MultipartFile.
     * @throws IOException Si une erreur survient lors du remplacement du fichier, ce qui peut inclure des problèmes d'accès aux fichiers.
     */
    private static void replaceFile(Path oldFilePath, Path newFilePath, MultipartFile multipartFile) throws IOException {
        // Vérifie si le fichier à remplacer existe. Si ce n'est pas le cas, une exception est lancée.
        if (!Files.exists(oldFilePath)) {
            throw new IOException("File does not exist");
        }

        // Supprime le fichier existant.
        Files.delete(oldFilePath);

        // Enregistre le nouveau fichier sur le serveur.
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not replace file", ioe);
        }
    }

    /**
     * Récupère un fichier en tant que ressource en utilisant le code unique du fichier.
     *
     * @param fileCode Le code unique du fichier pour localiser le fichier dans le répertoire de stockage.
     * @return La ressource du fichier trouvé, qui peut être utilisée pour lire le fichier ou le renvoyer dans une réponse HTTP.
     * @throws IOException Si une erreur survient lors de l'accès au fichier, ce qui peut inclure des problèmes de lecture du répertoire ou de création de l'URL.
     */
    public static Resource getFileAsResource(String fileCode) throws IOException {
        // Définit le chemin du répertoire d'enregistrement des fichiers en utilisant la constante FILES_UPLOAD_DIRECTORY.
        Path dirPath = Paths.get(FILES_UPLOAD_DIRECTORY);

        // Recherche le fichier correspondant au code unique dans le répertoire.
        Path foundFile;
        try (Stream<Path> stream = Files.list(dirPath)) {
            foundFile = stream.filter(file -> file.getFileName().toString().startsWith(fileCode))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        } catch (IOException e) {
            throw new InternalServerException("An error occurred while accessing the file directory.");
        }

        // Retourne la ressource du fichier si trouvé, sinon lance une exception.
        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        } else {
            throw new ResourceNotFoundException("File not found.");
        }
    }

    /**
     * Supprime un fichier en utilisant le code unique du fichier.
     *
     * @param fileCode Le code unique du fichier pour localiser les fichiers associés à ce code.
     * @throws IOException Si une erreur survient lors de la suppression du fichier, ce qui peut inclure des problèmes d'accès aux fichiers ou de suppression.
     */
    public static void deleteFile(String fileCode) throws IOException {
        // Définit le chemin du répertoire d'enregistrement des fichiers en utilisant la constante FILES_UPLOAD_DIRECTORY.
        Path uploadPath = Paths.get(FILES_UPLOAD_DIRECTORY);
        File directory = new File(String.valueOf(uploadPath));

        // Liste les fichiers dans le répertoire qui commencent par le code unique du fichier.
        File[] files = directory.listFiles((dir, name) -> name.startsWith(fileCode + "-"));
        if (files != null && files.length > 0) {
            for (File file : files) {
                // Supprime les fichiers trouvés.
                Files.deleteIfExists(file.toPath());
            }
        } else {
            throw new IOException("File not found with code");
        }
    }

}
