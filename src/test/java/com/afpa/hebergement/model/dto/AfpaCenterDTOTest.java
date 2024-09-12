package com.afpa.hebergement.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AfpaCenterDTOTest {

    private final Validator validator;

    AfpaCenterDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    /**
     * Fournit les données pour les tests paramétrés des numéros de téléphone.
     * Ces données couvrent les scénarios valides et invalides pour les numéros de téléphone.
     */
    private static Stream<Arguments> phoneNumberProvider() {
        return Stream.of(
                // Cas valides
                Arguments.of("+123456789", true, "Le numéro de téléphone avec + devrait être valide"),
                Arguments.of("123-456-7890", true, "Le numéro de téléphone avec des tirets devrait être valide"),
                Arguments.of("+1 (234) 567-8901", true, "Le numéro de téléphone avec des parenthèses et des espaces devrait être valide"),
                Arguments.of("123 456 7890", true, "Le numéro de téléphone avec des espaces devrait être valide"),
                Arguments.of("+12-3456-7890", true, "Le numéro de téléphone avec + et des tirets devrait être valide"),
                Arguments.of("+12 (345) 678-9012", true, "Le numéro de téléphone avec +, des espaces et des parenthèses devrait être valide"),

                // Cas invalides
                Arguments.of("12345", false, "Le numéro de téléphone trop court devrait être invalide"),
                Arguments.of("12345678901234567890123456", false, "Le numéro de téléphone trop long devrait être invalide"),
                Arguments.of("1234_5678", false, "Le numéro de téléphone avec des caractères non valides devrait être invalide"),
                Arguments.of("+123@456#7890", false, "Le numéro de téléphone avec des caractères spéciaux devrait être invalide"),
                Arguments.of("1234abcd5678", false, "Le numéro de téléphone contenant des lettres devrait être invalide"),
                Arguments.of("", false, "Le numéro de téléphone vide devrait être invalide")
        );
    }

    /**
     * Teste les numéros de téléphone avec différentes valeurs en utilisant des données paramétrées.
     *
     * @param phoneNumber Le numéro de téléphone à tester.
     * @param shouldBeValid Indique si le numéro de téléphone est valide ou non.
     * @param message Le message d'erreur attendu en cas de validation.
     */
    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("phoneNumberProvider")
    void testPhoneNumberValidation(String phoneNumber, boolean shouldBeValid, String message) {
        AfpaCenterDTO dto = new AfpaCenterDTO();
        dto.setCenterPhoneNum(phoneNumber);
        Set<ConstraintViolation<AfpaCenterDTO>> violations = validator.validate(dto);
        if (shouldBeValid) {
            assertTrue(violations.isEmpty(), message);
        } else {
            assertFalse(violations.isEmpty(), message);
        }
    }

    /**
     * Fournit les données pour les tests paramétrés des numéros de fax.
     * Ces données couvrent les scénarios valides et invalides pour les numéros de fax.
     */
    private static Stream<Arguments> faxNumberProvider() {
        return Stream.of(
                // Cas valides
                Arguments.of("+33 1 45 67 89 01", true, "Le numéro de fax avec +33 et espaces devrait être valide"),
                Arguments.of("01 45 67 89 01", true, "Le numéro de fax avec 01 et espaces devrait être valide"),
                Arguments.of("+33 1-45-67-89-01", true, "Le numéro de fax avec +33 et tirets devrait être valide"),
                Arguments.of("01-45-67-89-01", true, "Le numéro de fax avec 01 et tirets devrait être valide"),
                Arguments.of("+33 145 67 89 01", true, "Le numéro de fax avec +33 et un espace entre les groupes de chiffres devrait être valide"),
                Arguments.of("01 45 67 89 01", true, "Le numéro de fax avec 01 et des espaces devrait être valide"),
                Arguments.of("0145678901", true, "Le numéro de fax sans espaces ou tirets devrait être valide"),

                // Cas invalides
                Arguments.of("+33 01 45 67 89 01", false, "Le numéro de fax avec +33 et un format incorrect devrait être invalide"),
                Arguments.of("01-45-67-89-01-01", false, "Le numéro de fax avec trop de groupes devrait être invalide"),
                Arguments.of("+33 1 45 67 89 0A", false, "Le numéro de fax avec une lettre devrait être invalide"),
                Arguments.of("01-45-67-89-01-01", false, "Le numéro de fax avec un format incorrect devrait être invalide"),
                Arguments.of("", false, "Le numéro de fax vide devrait être invalide"),
                Arguments.of("+33 1 45 67 89 012", false, "Le numéro de fax avec un nombre incorrect de chiffres devrait être invalide"),
                Arguments.of("+33 1 45 67 89", false, "Le numéro de fax avec un nombre incorrect de chiffres devrait être invalide")
        );
    }

    /**
     * Teste les numéros de fax avec différentes valeurs en utilisant des données paramétrées.
     *
     * @param faxNumber Le numéro de fax à tester.
     * @param shouldBeValid Indique si le numéro de fax est valide ou non.
     * @param message Le message d'erreur attendu en cas de validation.
     */
    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("faxNumberProvider")
    void testFaxNumberValidation(String faxNumber, boolean shouldBeValid, String message) {
        AfpaCenterDTO dto = new AfpaCenterDTO();
        dto.setFaxCenter(faxNumber);
        Set<ConstraintViolation<AfpaCenterDTO>> violations = validator.validate(dto);
        if (shouldBeValid) {
            assertTrue(violations.isEmpty(), message);
        } else {
            assertFalse(violations.isEmpty(), message);
        }
    }

    /**
     * Fournit les données pour les tests paramétrés des numéros SIREN.
     * Ces données couvrent les scénarios valides et invalides pour les numéros SIREN.
     */
    private static Stream<Arguments> sirenNumberProvider() {
        return Stream.of(
                // Cas valides
                Arguments.of("123456789", true, "Le numéro SIREN avec 9 chiffres devrait être valide"),
                Arguments.of("987654321", true, "Le numéro SIREN avec 9 chiffres devrait être valide"),

                // Cas invalides
                Arguments.of("12345678", false, "Le numéro SIREN avec moins de 9 chiffres devrait être invalide"),
                Arguments.of("1234567890", false, "Le numéro SIREN avec plus de 9 chiffres devrait être invalide"),
                Arguments.of("12345678A", false, "Le numéro SIREN avec une lettre devrait être invalide"),
                Arguments.of("1234 567 89", false, "Le numéro SIREN avec des espaces devrait être invalide"),
                Arguments.of("123-456-789", false, "Le numéro SIREN avec des tirets devrait être invalide"),
                Arguments.of("", false, "Le numéro SIREN vide devrait être invalide"),
                Arguments.of("12345678A", false, "Le numéro SIREN avec une lettre devrait être invalide")
        );
    }

    /**
     * Teste les numéros SIREN avec différentes valeurs en utilisant des données paramétrées.
     *
     * @param sirenNumber Le numéro SIREN à tester.
     * @param shouldBeValid Indique si le numéro SIREN est valide ou non.
     * @param message Le message d'erreur attendu en cas de validation.
     */
    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("sirenNumberProvider")
    void testSirenNumberValidation(String sirenNumber, boolean shouldBeValid, String message) {
        AfpaCenterDTO dto = new AfpaCenterDTO();
        dto.setSiren(sirenNumber);
        Set<ConstraintViolation<AfpaCenterDTO>> violations = validator.validate(dto);
        if (shouldBeValid) {
            assertTrue(violations.isEmpty(), message);
        } else {
            assertFalse(violations.isEmpty(), message);
        }
    }

    /**
     * Fournit les données pour les tests paramétrés des numéros de TVA français.
     * Ces données couvrent les scénarios valides et invalides pour les numéros de TVA français.
     */
    private static Stream<Arguments> vatNumberProvider() {
        return Stream.of(
                // Cas valides
                Arguments.of("FR46824228142", true, "Le numéro de TVA avec le bon code de contrôle devrait être valide"),
                Arguments.of("FR46824228142", true, "Le numéro de TVA avec le bon code de contrôle sans espaces devrait être valide"),
                Arguments.of("FRXX000000000", true, "Le numéro de TVA avec deux lettres (XX) et neuf chiffres devrait être valide"),
                Arguments.of("FRST123456789", true, "Le numéro de TVA avec deux lettres (ST) et neuf chiffres devrait être valide"),
                Arguments.of("FRAB987654321", true, "Le numéro de TVA avec deux lettres (AB) et neuf chiffres devrait être valide"),
                Arguments.of("FR01123456789", true, "Le numéro de TVA avec deux lettres (01) et neuf chiffres devrait être valide"),

                // Cas invalides
                Arguments.of("FR14 824 228 142", false, "Le numéro de TVA avec le mauvais code de contrôle devrait être invalide"),
                Arguments.of("FR12 345 678 901", false, "Le numéro de TVA avec un mauvais code de contrôle devrait être invalide"),
                Arguments.of("FRAB12345678", false, "Le numéro de TVA avec deux lettres (AB) et huit chiffres devrait être invalide"),
                Arguments.of("FRAB1234567890", false, "Le numéro de TVA avec deux lettres (AB) et dix chiffres devrait être invalide"),
                Arguments.of("FRAB12345678A", false, "Le numéro de TVA avec deux lettres (AB) et une lettre dans le numéro devrait être invalide"),
                Arguments.of("FR12A3456789", false, "Le numéro de TVA avec des caractères non valides devrait être invalide"),
                Arguments.of("123456789", false, "Le numéro de TVA sans code pays devrait être invalide"),
                Arguments.of("", false, "Le numéro de TVA vide devrait être invalide"),
                Arguments.of("FRAB1234ABCD", false, "Le numéro de TVA avec des lettres dans le numéro devrait être invalide")
        );
    }

    /**
     * Teste les numéros de TVA avec différentes valeurs en utilisant des données paramétrées.
     *
     * @param vatNumber Le numéro de TVA à tester.
     * @param shouldBeValid Indique si le numéro de TVA est valide ou non.
     * @param message Le message d'erreur attendu en cas de validation.
     */
    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("vatNumberProvider")
    void testVatNumberValidation(String vatNumber, boolean shouldBeValid, String message) {
        AfpaCenterDTO dto = new AfpaCenterDTO();
        dto.setTvaNumber(vatNumber);
        Set<ConstraintViolation<AfpaCenterDTO>> violations = validator.validate(dto);
        if (shouldBeValid) {
            assertTrue(violations.isEmpty(), message);
        } else {
            assertFalse(violations.isEmpty(), message);
        }
    }
}
