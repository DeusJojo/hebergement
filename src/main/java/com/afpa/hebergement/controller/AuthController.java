package com.afpa.hebergement.controller;


import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.entity.Intendant;
import com.afpa.hebergement.model.entity.Role;
import com.afpa.hebergement.model.enum_role.RoleType;
import com.afpa.hebergement.model.repository.*;
import com.afpa.hebergement.security.jwt.JwtUtils;
import com.afpa.hebergement.model.payload.request.LoginRequest;
import com.afpa.hebergement.model.payload.request.SignupRequest;
import com.afpa.hebergement.model.payload.response.JwtResponse;
import com.afpa.hebergement.model.payload.response.MessageResponse;
import com.afpa.hebergement.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 1800)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    AuthenticationManager authManager;
    private static final String ROLE_NOT_FOUND = "Error : role not found";
    AppUserRepository appUserRepository;
    CivilityRepository civilityRepository;
    ContactPersonRepository contactRepo;
    RoleRepository roleRepository;
    AfpaCenterRepository centerRepo;
    CityRepository cityRepo;
    PasswordEncoder encoder;
    IntendantRepository intendantRepository;

    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getNumber(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getBeneficiaryNumber(),
                userDetails.getEmail(),
                userDetails.getIdAfpaCenter(),
                roles
        ));
    }


    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (appUserRepository.existsByBeneficiaryNumber(signUpRequest.getNumber())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (appUserRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        DateTimeFormatter.ofPattern("dd-MM-yyyy");
        AppUser user = new AppUser(signUpRequest.getFirstName(), signUpRequest.getName(), LocalDate.parse(signUpRequest.getBirthdate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")), signUpRequest.getBirthPlace(), signUpRequest.getNumber(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        user.setIdAfpaCenter(centerRepo.findById(signUpRequest.getIdCenter()).orElseThrow(() -> new NoContentException("No Center found")));
        user.setIdCity(cityRepo.findByInseeCodeAndPostcode(signUpRequest.getInseeCode(), signUpRequest.getPostCode()).orElseThrow(() -> new NoContentException("No city found")));
        user.setIdCivility(civilityRepository.findById(signUpRequest.getCivility()).orElseThrow(() -> new NoContentException("Civility not found")));
        user.setRegistrationDate(LocalDate.now());
        user.setIdContactPerson(contactRepo.findById(signUpRequest.getIdContactPerson()).orElseThrow(() -> new NoContentException("Contact not found ")));
        user.setIsBlacklisted(false);
        user.setPhoneNumber(signUpRequest.getPhone());
        user.setAddressUser(signUpRequest.getAddress());

        Role strRoles = roleRepository.findByWordingRole(RoleType.valueOf(signUpRequest.getRole())).orElseThrow(() -> new NoContentException("No such role"));
        Role roles;

        if (strRoles == null) {

            roles = roleRepository.findByWordingRole(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
        } else {

            roles = switch (strRoles.getWordingRole()) {
                case ROLE_INTENDANT ->
                        roleRepository.findByWordingRole(RoleType.ROLE_INTENDANT).orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                case ROLE_ADMIN ->
                        roleRepository.findByWordingRole(RoleType.ROLE_ADMIN).orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                default ->
                        roleRepository.findByWordingRole(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
            };

        }

        user.setIdRole(roles);
        appUserRepository.save(user);

        if (user.getIdRole().getWordingRole().equals(RoleType.ROLE_INTENDANT) || user.getIdRole().getWordingRole().equals(RoleType.ROLE_ADMIN)) {
            // Créer une nouvelle instance de l'entité Intendant et associer l'utilisateur
            Intendant intendant = new Intendant();
            //associer l'utilisateur
            intendant.setIdUser(user);
            //enregistrement dans l'entité

            intendantRepository.save(intendant);
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}