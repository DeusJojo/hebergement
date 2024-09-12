package com.afpa.hebergement.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FormatUtilTest {

    @Test
    void setAmount() {
        // Cas de base avec arrondi à deux chiffres après la virgule
        assertEquals(new BigDecimal("70.30"), FormatUtil.setAmount(new BigDecimal("70.30468")));

        // Arrondi vers le haut
        assertEquals(new BigDecimal("0.31"), FormatUtil.setAmount(new BigDecimal("0.30668")));

        // Pas de chiffres significatifs après la virgule
        assertEquals(new BigDecimal("11"), FormatUtil.setAmount(new BigDecimal("11.0012365")));

        // Valeur entière sans chiffres après la virgule
        assertEquals(new BigDecimal("70"), FormatUtil.setAmount(new BigDecimal("70.0000000")));

        // Un seul zéro après la virgule
        assertEquals(new BigDecimal("70.10"), FormatUtil.setAmount(new BigDecimal("70.1000000")));

        // Deux zéros après la virgule
        assertEquals(new BigDecimal("70"), FormatUtil.setAmount(new BigDecimal("70.00")));

        // Valeur avec plusieurs chiffres non-zéro après la virgule
        assertEquals(new BigDecimal("70.12"), FormatUtil.setAmount(new BigDecimal("70.123456")));

        // Valeur négative avec arrondi
        assertEquals(new BigDecimal("-70.30"), FormatUtil.setAmount(new BigDecimal("-70.30468")));

        // Valeur négative avec arrondi vers le haut
        assertEquals(new BigDecimal("-0.31"), FormatUtil.setAmount(new BigDecimal("-0.30668")));

        // Valeur négative sans chiffres significatifs après la virgule
        assertEquals(new BigDecimal("-11"), FormatUtil.setAmount(new BigDecimal("-11.0012365")));

        // Valeur négative entière sans chiffres après la virgule
        assertEquals(new BigDecimal("-70"), FormatUtil.setAmount(new BigDecimal("-70.0000000")));

        // Valeur négative avec un seul zéro après la virgule
        assertEquals(new BigDecimal("-70.10"), FormatUtil.setAmount(new BigDecimal("-70.1000000")));

        // Valeur négative avec deux zéros après la virgule
        assertEquals(new BigDecimal("-70"), FormatUtil.setAmount(new BigDecimal("-70.00")));

        // Valeur null
        assertNull(FormatUtil.setAmount(null));

        // Valeur zéro
        assertEquals(new BigDecimal("0"), FormatUtil.setAmount(new BigDecimal("0.0000000")));

        // Valeur zéro avec deux chiffres après la virgule
        assertEquals(new BigDecimal("0"), FormatUtil.setAmount(new BigDecimal("0.004")));

        // Valeur entière
        assertEquals(new BigDecimal("100"), FormatUtil.setAmount(new BigDecimal("100")));

        // Valeur entière avec des chiffres après la virgule
        assertEquals(new BigDecimal("100.12"), FormatUtil.setAmount(new BigDecimal("100.123456")));
    }
}
