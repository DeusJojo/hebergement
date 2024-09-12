package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.AppServiceDTO;
import com.afpa.hebergement.service.entity_service.AppServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour gérer les opérations liées aux services.
 */
@Tag(name = "Service Controller", description = "Points de terminaison pour gérer les services")
@RestController
@AllArgsConstructor
@RequestMapping("/api/services")
public class AppServiceController {

    private final AppServiceService appServiceService;

    /**
     * Point de terminaison pour créer un nouveau service.
     *
     * @param appServiceDto L'objet AppServiceDTO contenant les détails du service.
     * @return ResponseEntity contenant le AppServiceDTO créé et le statut HTTP.
     */
    @Operation(description = "Create a service")
    @PostMapping("/create")
    public ResponseEntity<AppServiceDTO> create(@RequestBody AppServiceDTO appServiceDto){
        AppServiceDTO savedAppServiceDto = appServiceService.create(appServiceDto);
        return new ResponseEntity<>(savedAppServiceDto, HttpStatus.CREATED);
    }

    /**
     * Récupère tous les services associés à un centre AFPA spécifique via son identifiant.
     *
     * @param idAfpaCenter L'identifiant du centre AFPA pour lequel récupérer les services.
     * @return ResponseEntity contenant une liste de tous les AppServiceDTO et le statut HTTP.
     */
    @Operation(description = "Get all services by afpa center id")
    @GetMapping("/getAllByIdAfpaCenter/{id}")
    public ResponseEntity<List<AppServiceDTO>> getAllByIdAfpaCenter(@PathVariable("id") Integer idAfpaCenter) {
        List<AppServiceDTO> appServiceDtoList = appServiceService.getAllByAfpaCenterId(idAfpaCenter);
        return new ResponseEntity<>(appServiceDtoList, HttpStatus.OK);
    }

    /**
     * Point de terminaison pour obtenir un service par son identifiant.
     *
     * @param id L'identifiant du service.
     * @return ResponseEntity contenant le AppServiceDTO correspondant et le statut HTTP, ou le statut HTTP NOT FOUND si le service n'est pas trouvé.
     */
    @Operation(description = "Get a service by his id")
    @GetMapping("/{id}")
    public ResponseEntity<AppServiceDTO> getById(@PathVariable("id") Integer id) {
        Optional<AppServiceDTO> appServiceDtoOptional = appServiceService.getById(id);
        return appServiceDtoOptional.map(appServiceDto -> new ResponseEntity<>(appServiceDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Point de terminaison pour mettre à jour un service.
     *
     * @param id L'identifiant du service à mettre à jour.
     * @param appServiceDto L'objet AppServiceDTO contenant les nouveaux détails du service.
     * @return ResponseEntity contenant le AppServiceDTO mis à jour et le statut HTTP, ou le statut HTTP NOT FOUND si le service n'est pas trouvé.
     */
    @Operation(description = "Update a service")
    @PutMapping("/update/{id}")
    public ResponseEntity<AppServiceDTO> update(@PathVariable("id") Integer id,
                                                @RequestBody AppServiceDTO appServiceDto) {
        Optional<AppServiceDTO> updateAppServiceDtoOptional = appServiceService.update(id, appServiceDto);
        return updateAppServiceDtoOptional.map(updateAppServiceDto -> new ResponseEntity<>(updateAppServiceDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Point de terminaison pour supprimer un service.
     *
     * @param id L'identifiant du service à supprimer.
     * @return ResponseEntity avec le statut HTTP NO CONTENT.
     */
    @Operation(description = "Delete a service")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Integer id) {
        appServiceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
