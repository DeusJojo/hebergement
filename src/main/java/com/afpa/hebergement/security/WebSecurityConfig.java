package com.afpa.hebergement.security;

import com.afpa.hebergement.security.jwt.AuthEntryPointJwt;
import com.afpa.hebergement.security.jwt.AuthTokenFilter;
import com.afpa.hebergement.security.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // Active la sécurité Web dans l'application Spring
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true) // Active la sécurité au niveau des méthodes. Permet l'utilisation des annotations comme @Secured, @RolesAllowed, @PreAuthorize et @PostAuthorize.
@AllArgsConstructor // Génère un constructeur avec tous les arguments pour les champs privés de la classe
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService; // Service personnalisé pour gérer les détails de l'utilisateur
    private final AuthEntryPointJwt unauthorizedHandler; // Gestionnaire pour les erreurs d'authentification

    // Définition du filtre d'authentification JWT
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter(); // Retourne une instance du filtre JWT
    }

    // Configuration de l'authentification via DAO avec encodage des mots de passe
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Définit le service pour charger les détails de l'utilisateur
        authProvider.setPasswordEncoder(passwordEncoder()); // Définit l'encodeur de mot de passe
        return authProvider;
    }

    // Gestionnaire d'authentification
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Retourne le gestionnaire d'authentification configuré
    }

    // Encodeur de mots de passe (BCrypt avec une force de 12)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Utilise BCrypt avec un facteur de force de 12 pour l'encodage des mots de passe
    }

    // Chaîne de filtres de sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())) // Configuration CORS
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Gestion des exceptions d'authentification
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configuration des sessions comme stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/test/**").permitAll() // Autorisation pour certains endpoints publics
                        .requestMatchers(HttpMethod.GET, "/csrf/token").permitAll()
                        .anyRequest().permitAll() // Toutes les autres requêtes sont également autorisées
                )
                .httpBasic(AbstractHttpConfigurer::disable) // Désactivation de l'authentification HTTP Basic
                .authenticationProvider(authenticationProvider()) // Configuration du fournisseur d'authentification
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Ajout du filtre JWT avant le filtre d'authentification par nom d'utilisateur et mot de passe

        return http.build(); // Construction et retour de la chaîne de filtres de sécurité
    }

    // Configuration CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // Création d'une nouvelle configuration CORS
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Autorisation des requêtes depuis le front-end Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes HTTP autorisées
        configuration.setAllowedHeaders(List.of("*")); // Autorisation de tous les en-têtes
        configuration.setExposedHeaders(List.of("Content-Disposition")); // Exposition de l'en-tête Content-Disposition
        configuration.setAllowCredentials(true); // Autorisation des cookies d'authentification

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Application de la configuration à toutes les requêtes

        return source; // Retour de la source de configuration CORS
    }

    // Gestion de la hiérarchie des rôles
    @Bean
    public RoleHierarchy roleHierarchy() {
        // Retourne une hiérarchie de rôles définie
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_INTENDANT\nROLE_INTENDANT > ROLE_USER");
    }

    // Gestionnaire d'expressions de sécurité pour la hiérarchie des rôles
    // Utilisé pour évaluer les expressions de sécurité (ex : "hasRole('ROLE_ADMIN')") dans les annotations comme @PreAuthorize, @PostAuthorize
    @Bean
    public MethodSecurityExpressionHandler expressionHandler(RoleHierarchy roleHierarchy) {
        // Crée un gestionnaire d'expressions de sécurité par défaut
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();

        // Injecte la hiérarchie des rôles dans le gestionnaire d'expressions de sécurité
        expressionHandler.setRoleHierarchy(roleHierarchy);

        // Retourne le gestionnaire d'expressions de sécurité configuré
        return expressionHandler;
    }
}
