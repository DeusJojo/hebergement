package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.DocumentCenterDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.DocumentCenter;
import com.afpa.hebergement.model.entity.DocumentType;
import com.afpa.hebergement.model.mapper.DocumentCenterMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.DocumentCenterRepository;
import com.afpa.hebergement.model.repository.DocumentTypeRepository;
import com.afpa.hebergement.service.entity_service.DocumentCenterService;
import com.afpa.hebergement.util.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service DocumentCenterService.
 * Cette classe fournit des méthodes pour gérer les documents associés aux centres Afpa, y compris le téléchargement,
 * la mise à jour, et la suppression des documents.
 */
@Service
@AllArgsConstructor
public class DocumentCenterServiceImpl implements DocumentCenterService {

    private final AfpaCenterRepository afpaCenterRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentCenterRepository documentCenterRepository;

    private static final String NO_DOCUMENT_CENTER_FOUND = "Document center does not exist";
    private static final String AN_ERROR_OCCURRED = "An error occurred.";

    /**
     * Méthode pour télécharger un fichier et créer une entrée DocumentCenter correspondante.
     * Cette méthode permet de télécharger un fichier PDF et de créer une nouvelle entrée de document associée à un centre AFPA et un type de document.
     * @param multipartFile  Le fichier à télécharger. Doit être un fichier PDF non vide.
     * @param idAfpaCenter   L'identifiant du centre Afpa auquel le document doit être associé.
     * @param idDocumentType L'identifiant du type de document auquel le document doit être associé.
     * @param commentary     Le commentaire associé au document.
     * @return Le DTO du document créé encapsulé dans un {@link DocumentCenterDTO}.
     * @throws CreationException Si le fichier est nul, vide, ou n'est pas un PDF, ou si le nom du fichier est nul ou vide.
     * @throws ResourceNotFoundException Si le centre AFPA ou le type de document correspondant à l'identifiant fourni n'existe pas.
     * @throws InternalServerException En cas d'erreur lors de l'enregistrement du fichier ou de la création du document.
     */
    @Override
    public DocumentCenterDTO uploadFile(MultipartFile multipartFile, Integer idAfpaCenter, Integer idDocumentType, String commentary) {
        // Vérifie si le fichier est nul ou vide, et lance une exception si c'est le cas
        if (multipartFile == null || multipartFile.isEmpty())
            throw new CreationException("Invalid document center provided.");

        // Vérifie si le fichier est un PDF
        String fileType = multipartFile.getContentType();
        if(fileType == null || !fileType.equals("application/pdf"))
            throw new CreationException("The uploaded file is not a PDF");

        String fileName = multipartFile.getOriginalFilename();
        // Vérifie si le nom du fichier est nul ou vide, et lance une exception si c'est le cas
        if (fileName == null || fileName.isEmpty())
            throw new CreationException("The file name is empty");

        // Récupère le centre afpa par son id reçu en paramètre
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City does not exist"));

        // Récupère le type de document par son id reçu en paramètre
        DocumentType documentType = documentTypeRepository.findById(idDocumentType)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Document type does not exist"));

        try {
            // Nettoie le nom du fichier pour éviter les problèmes de chemin
            fileName = StringUtils.cleanPath(fileName);

            // Enregistre le fichier dans le répertoire des fichiers téléchargés et récupère son code de fichier
            String fileCode = FileUtil.saveFile(fileName, multipartFile);

            DocumentCenter documentCenter = new DocumentCenter();
            documentCenter.setDocumentName(fileName);
            documentCenter.setDocumentCode(fileCode);
            documentCenter.setDateDepoDoc(LocalDate.now());
            documentCenter.setDocCommentary(commentary);
            documentCenter.setIdAfpaCenter(afpaCenter);
            documentCenter.setIdDocumentType(documentType);
            DocumentCenter savedDocument = documentCenterRepository.save(documentCenter);
            return DocumentCenterMapper.mapToDocumentCenterDTO(savedDocument);
        } catch (DataIntegrityViolationException e) {
            throw new CreationException("Unable to create a document center due to a data integrity issue.");
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    @Override
    public Page<DocumentCenterDTO> getAllByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<DocumentCenter> documentsCenterPage = documentCenterRepository.findByIdAfpaCenter_Id(idAfpaCenter, pageable);

        if (documentsCenterPage.isEmpty()) {
            throw new NoContentException("Documents not found");
        }

        try {
            // Mappe les entités users en DTO et retourne la page
            return documentsCenterPage.map(DocumentCenterMapper::mapToDocumentCenterDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting user(s) by pagination");
        }
    }

    /**
     * Méthode pour obtenir tous les documents du centre par type de document.
     * Cette méthode récupère tous les documents correspondant au type de document spécifié.
     *
     * @param idDocumentType Le libellé du type de document.
     * @return Une liste de {@link DocumentCenterDTO} correspondant au type de document spécifié.
     * @throws InternalServerException Si une erreur survient lors de la récupération des documents depuis la base de données.
     */
    @Override
    public List<DocumentCenterDTO> getAllByDocumentTypeAndIdAfpaCenter(Integer idDocumentType, Integer idAfpaCenter) {
        try {
            List<DocumentCenter> documentCenterList = documentCenterRepository.
                    findAllByIdDocumentType_IdAndIdAfpaCenter_Id(idDocumentType, idAfpaCenter);
            return documentCenterList.stream().map(DocumentCenterMapper::mapToDocumentCenterDTO).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère tous les documents associés à un centre AFPA spécifique par son identifiant.
     * Cette méthode récupère tous les documents associés au centre AFPA spécifié par son identifiant unique.
     *
     * @param idAfpaCenter L'identifiant du centre AFPA pour lequel les documents doivent être récupérés.
     * @return Une liste de {@link DocumentCenterDTO} représentant les documents associés au centre AFPA spécifié.
     * @throws InternalServerException Si une erreur survient lors de la récupération des documents depuis la base de données.
     */
    @Override
    public List<DocumentCenterDTO> getAllByAfpaCenterId(Integer idAfpaCenter) {
        try {
            List<DocumentCenter> documentCenterList = documentCenterRepository.findAllByIdAfpaCenter_Id(idAfpaCenter);
            return documentCenterList.stream().map(DocumentCenterMapper::mapToDocumentCenterDTO).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Méthode pour mettre à jour un fichier existant et ses informations associées.
     * Cette méthode permet de télécharger un nouveau fichier PDF pour remplacer un fichier existant,
     * et de mettre à jour les informations associées au document.
     * @param multipartFile  Le nouveau fichier à télécharger. Doit être un fichier PDF non vide.
     * @param idDocumentType L'identifiant du type de document auquel le document doit être associé.
     * @param commentary     Le commentaire associé au document.
     * @param id             L'identifiant du document à mettre à jour.
     * @return Un {@link Optional} contenant le {@link DocumentCenterDTO} mis à jour, ou vide si le document n'existe pas.
     * @throws CreationException Si le fichier n'est pas un PDF ou si le nom du fichier est nul ou vide.
     * @throws ResourceNotFoundException Si le document à mettre à jour ou le type de document correspondant à l'identifiant fourni n'existe pas.
     * @throws InternalServerException En cas d'erreur lors de la mise à jour du fichier ou des informations du document.
     */
    @Override
    public Optional<DocumentCenterDTO> updateFile(MultipartFile multipartFile, Integer idDocumentType,
                                                  String commentary, Integer id) {

        // Récupère le document à modifier par son identifiant
        DocumentCenter documentCenter = documentCenterRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NO_DOCUMENT_CENTER_FOUND));

        // Récupère le type de document par son id reçu en paramètre
        DocumentType documentType = documentTypeRepository.findById(idDocumentType)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NO_DOCUMENT_CENTER_FOUND));

        // Vérifie si le fichier existe
        if(multipartFile != null){

            // Récupère le type du fichier (pdf, txt...)
            String fileType = multipartFile.getContentType();

            // Vérifie si le fichier est un PDF
            if (fileType != null && !fileType.equals("application/pdf"))
                throw new CreationException("The uploaded file is not a PDF");

            String newFileName = multipartFile.getOriginalFilename();

            // Vérifie si le nom du fichier est nul ou vide, et lance une exception si c'est le cas
            if (newFileName == null || newFileName.isEmpty())
                throw new CreationException("The file name is empty");
            // Nettoie le nom du fichier pour éviter les problèmes de chemin
            newFileName = StringUtils.cleanPath(newFileName);

            // Récupère le nom du document actuellement et celui du nouveau document
            String oldFileName = documentCenter.getDocumentName();
            oldFileName = StringUtils.cleanPath(oldFileName);

            try {
                // Modifie le fichier dans le répertoire Files-Upload du serveur
                FileUtil.updateFile(oldFileName, newFileName, multipartFile, documentCenter.getDocumentCode());
            } catch (Exception e) {
                throw new InternalServerException(AN_ERROR_OCCURRED);
            }
            documentCenter.setDocumentName(newFileName);
        }

        try {
            documentCenter.setDateDepoDoc(LocalDate.now());
            documentCenter.setDocCommentary(commentary);
            documentCenter.setIdDocumentType(documentType);
            DocumentCenter updatedDocument = documentCenterRepository.save(documentCenter);
            return Optional.of(DocumentCenterMapper.mapToDocumentCenterDTO(updatedDocument));
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }


    /**
     * Méthode pour obtenir un DocumentCenter par son identifiant.
     * Cette méthode récupère un document spécifique par son identifiant unique et le convertit en DTO pour la réponse.
     * @param id L'identifiant du document.
     * @return Un {@link Optional} contenant le {@link DocumentCenterDTO} si le document est trouvé, sinon vide.
     * @throws ResourceNotFoundException Si aucun document n'existe avec l'identifiant fourni.
     */
    @Override
    public Optional<DocumentCenterDTO> getById(Integer id) {
        DocumentCenter documentCenter = documentCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_DOCUMENT_CENTER_FOUND));
        return Optional.of(DocumentCenterMapper.mapToDocumentCenterDTO(documentCenter));
    }

    /**
     * Méthode pour supprimer un DocumentCenter par son identifiant.
     * Cette méthode supprime le document correspondant à l'identifiant fourni et efface le fichier associé du serveur.
     * @param id L'identifiant du document à supprimer.
     * @throws ResourceNotFoundException Si aucun document n'existe avec l'identifiant fourni.
     * @throws InternalServerException En cas d'erreur lors de la suppression du fichier ou du document.
     */
    @Override
    public void deleteById(Integer id) {
        DocumentCenter documentCenter = documentCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_DOCUMENT_CENTER_FOUND));
        try {
            FileUtil.deleteFile(documentCenter.getDocumentCode());
            documentCenterRepository.delete(documentCenter);
        } catch (Exception e) {
            throw new InternalServerException("Une erreur s'est produite.");
        }
    }
}
