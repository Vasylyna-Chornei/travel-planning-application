package com.vasylyna.travelplanningapplication.util;

public class SpellingUtil {
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String words[] = input.trim().split("\\s+");

        if (words[0].toUpperCase().contains("США")) {
            return words[0].toUpperCase();
        }
        if (words.length == 1 && !words[0].contains("-")) {
            return capitalizeWord(words[0]);
        }

        if (words.length == 2) {
            return capitalizeWord(words[0]) + " " + capitalizeWord(words[1]);
        }

        return input;
    }

    private static String capitalizeWord(String word) {
        if (word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
