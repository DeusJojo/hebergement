package com.afpa.hebergement.util;

/**
 * Classe pour formater les Strings
 */
public class StringUtil {

    private StringUtil() {}

    /**
     * Formate les prénoms. 1ère lettre en majuscule, le reste en minuscule.
     * Prend en compte les prénoms composés.
     * @param str la chaine d'entrée à capitaliser
     * @return La chaine d'entrée avec chaque mot capitalisé
     */
    public static String capitalize(String str) {
        // Si null renvoie la chaîne
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Convertir la chaîne d'entrée en minuscules pour normalisation
        String normalized = str.toLowerCase();

        StringBuilder stringBuilder = new StringBuilder();
        return verifySeparator(stringBuilder, normalized).toString();
    }

    /**
     * Fonction auxiliaire pour capitaliser les mots séparés par ' ' ou '-'.
     * Ajoute les mots capitalisés au StringBuilder avec les séparateurs corrects.
     *
     * @param stringBuilder Le StringBuilder pour construire la sortie
     * @param normalized La chaîne normalisée (en minuscules)
     * @return Le StringBuilder avec les mots capitalisés et les séparateurs corrects
     */
    private static StringBuilder verifySeparator(StringBuilder stringBuilder, String normalized) {
        int len = normalized.length();

        // Indicateur pour savoir si le prochain caractère doit être capitalisé
        boolean capitalizeNext = true;

        for (int i = 0; i < len; i++) {
            char c = normalized.charAt(i);
            if (c == ' ' || c == '-' || c == '\'') {
                // Ajouter les séparateurs tels quels et indiquer que le prochain caractère doit être capitalisé
                stringBuilder.append(c);
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    // Capitaliser le premier caractère d'un nouveau mot
                    stringBuilder.append(Character.toUpperCase(c));
                    // Le prochain caractère ne doit pas être capitalisé
                    capitalizeNext = false;
                } else {
                    // Ajouter les caractères suivants en minuscules
                    stringBuilder.append(c);
                }
            }
        }

        return stringBuilder;
    }

    /**
     * Supprime les espaces d'un String.
     * @param str la chaine d'entrée à modifier
     * @return La chaine d'entrée sans espace
     */
    public static String deleteSpace(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * Met en majuscule le premier caractère d'un String.
     * @param str la chaine d'entrée à modifier
     * @return La chaine d'entrée sans espace
     */
    public static String capitalizeFirstLetter(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
