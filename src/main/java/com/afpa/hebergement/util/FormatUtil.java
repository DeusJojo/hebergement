package com.afpa.hebergement.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe utilitaire pour formater des données.
 */
public class FormatUtil {

    // Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
    private FormatUtil() {}

    /**
     * Formate un montant en arrondissant à deux chiffres après la virgule.
     * Si les deux chiffres après la virgule sont des zéros, retourne le montant sans chiffres après la virgule.
     *
     * @param amount Le montant à formater
     * @return Le montant formaté
     */
    public static BigDecimal setAmount(BigDecimal amount) {
        if (amount == null) {
            return null;
        }

        // Arrondir à deux chiffres après la virgule
        BigDecimal scaledAmount = amount.setScale(2, RoundingMode.HALF_UP);

        // Vérifier si les deux chiffres après la virgule sont des zéros
        if (scaledAmount.stripTrailingZeros().scale() <= 0) {
            return scaledAmount.setScale(0, RoundingMode.UNNECESSARY);
        }

        // Retourner le montant formaté
        return scaledAmount;
    }
}
