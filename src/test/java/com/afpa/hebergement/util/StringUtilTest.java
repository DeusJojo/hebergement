package com.afpa.hebergement.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

/** TEST CAPITALIZE */

    @Test
    void testCapitalizeNullInput() {
        assertNull(StringUtil.capitalize(null));
    }

    @Test
    void testCapitalizeEmptyInput() {
        assertEquals("", StringUtil.capitalize(""));
    }

    @Test
    void testCapitalizeSingleWord() {
        assertEquals("John", StringUtil.capitalize("john"));
        assertEquals("John", StringUtil.capitalize("JOHN"));
        assertEquals("John", StringUtil.capitalize("jOhN"));
    }

    @Test
    void testCapitalizeHyphenatedName() {
        assertEquals("Jean-Pierre", StringUtil.capitalize("jean-pierre"));
        assertEquals("Jean-Pierre", StringUtil.capitalize("JEAN-PIERRE"));
        assertEquals("Jean-Pierre", StringUtil.capitalize("Jean-Pierre"));
    }

    @Test
    void testCapitalizeSpaceSeparatedName() {
        assertEquals("Jean Pierre", StringUtil.capitalize("jean pierre"));
        assertEquals("Jean Pierre", StringUtil.capitalize("JEAN PIERRE"));
        assertEquals("Jean Pierre", StringUtil.capitalize("Jean Pierre"));
    }

    @Test
    void testCapitalizeMixedSeparators() {
        assertEquals("Jean Pierre-Paul", StringUtil.capitalize("jean pierre-paul"));
    }

    @Test
    void testCapitalizeApostrophe() {
        assertEquals("O'Neil", StringUtil.capitalize("o'neil"));
    }

    /** TEST DELETE SPACE */

    @Test
    void testDeleteSpaceNullInput() {
        assertNull(StringUtil.deleteSpace(null));
    }

    @Test
    void testDeleteSpaceEmptyInput() {
        assertEquals("", StringUtil.deleteSpace(""));
    }

    @Test
    void testDeleteSpaceSingleWord() {
        assertEquals("word", StringUtil.deleteSpace("word"));
    }

    @Test
    void testDeleteSpaceWithSpaces() {
        assertEquals("word", StringUtil.deleteSpace(" w o r d "));
        assertEquals("word", StringUtil.deleteSpace(" w   o  r  d "));
        assertEquals("word", StringUtil.deleteSpace("  word  "));
    }

    @Test
    void testDeleteSpaceWithTabs() {
        assertEquals("word", StringUtil.deleteSpace("\tword\t"));
        assertEquals("word", StringUtil.deleteSpace("\t w\to\tr\td\t"));
    }

    @Test
    void testDeleteSpaceWithNewLines() {
        assertEquals("word", StringUtil.deleteSpace("\nword\n"));
        assertEquals("word", StringUtil.deleteSpace("\n w \no \nr \nd \n"));
    }

    @Test
    void testDeleteSpaceWithMixedSpaces() {
        assertEquals("wordword", StringUtil.deleteSpace("word word"));
        assertEquals("wordword", StringUtil.deleteSpace("word   word"));
        assertEquals("wordword", StringUtil.deleteSpace("  word  word  "));
    }

    /** TEST CAPITALIZE FIRST LETTER */

    @Test
    void testCapitalizeFirstLetterNullInput() {
        assertNull(StringUtil.capitalizeFirstLetter(null));
    }

    @Test
    void testCapitalizeFirstLetterEmptyInput() {
        assertEquals("", StringUtil.capitalizeFirstLetter(""));
    }

    @Test
    void testCapitalizeFirstLetterSingleLowercaseCharacter() {
        assertEquals("A", StringUtil.capitalizeFirstLetter("a"));
    }

    @Test
    void testCapitalizeFirstLetterSingleUppercaseCharacter() {
        assertEquals("A", StringUtil.capitalizeFirstLetter("A"));
    }

    @Test
    void testCapitalizeFirstLetterMultipleCharactersStartingWithLowercase() {
        assertEquals("Hello", StringUtil.capitalizeFirstLetter("hello"));
    }

    @Test
    void testCapitalizeFirstLetterMultipleCharactersStartingWithUppercase() {
        assertEquals("Hello", StringUtil.capitalizeFirstLetter("Hello"));
    }

    @Test
    void testCapitalizeFirstLetterCharactersIncludingDigits() {
        assertEquals("Hello123", StringUtil.capitalizeFirstLetter("hello123"));
        assertEquals("123hello", StringUtil.capitalizeFirstLetter("123hello"));
    }

    @Test
    void testCapitalizeFirstLetterCharactersIncludingSpecialCharacters() {
        assertEquals("!hello", StringUtil.capitalizeFirstLetter("!hello"));
    }

    @Test
    void testCapitalizeFirstLetterHyphenatedName() {
        assertEquals("Jean-pierre", StringUtil.capitalizeFirstLetter("jean-pierre"));
        assertEquals("JEAN-PIERRE", StringUtil.capitalizeFirstLetter("JEAN-PIERRE"));
    }

    @Test
    void testCapitalizeFirstLetterSpaceSeparatedName() {
        assertEquals("Jean pierre", StringUtil.capitalizeFirstLetter("jean pierre"));
        assertEquals("JEAN PIERRE", StringUtil.capitalizeFirstLetter("JEAN PIERRE"));
    }

    @Test
    void testCapitalizeFirstLetterApostrophe() {
        assertEquals("O'neil", StringUtil.capitalizeFirstLetter("o'neil"));
    }
}
