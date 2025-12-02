package com.uwuScape;
//literally just copypasted this file from idyl, just added more stuff, fixed some issues, added options and the like
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Owoify {
    private static Map<String, String> wordMap = new HashMap();
    private static List<String> prefixes = Arrays.asList(
            "OwO whats this? ",
            "*nuzzles* ",
            "*waises paw* ",
            "*blushes* ",
            "*giggles* ",
            "hehe ",
            "rawr~ ",
            "rawr x3",
            "teehee ",
            "*boops your nose* ",
            "*runs in circles* ",
            "H-hewwo?? ",
            "*licks paw* ",
            "nya~ ",
            "*shakes tail* ",
            "gib pats pws! "
            );

    private static final List<String> emojiSuffixes = Arrays.asList("~", " :3", " x3", " ^_^", " UwU", " owo", "OwO", "uwu");

    static {
        wordMap.put("love", "wuv");
        wordMap.put("morning", "meowning");
        wordMap.put("beauty", "bewti");
        wordMap.put("mr", "mistuh");
        wordMap.put("dog", "doggo");
        wordMap.put("cat", "kitteh");
        wordMap.put("hello", "henwo");
        wordMap.put("hi", "haiii~");
        wordMap.put("hey", "heya~");
        wordMap.put("hell", "heck");
        wordMap.put("fuck", "fwick");
        wordMap.put("fuk", "fwick");
        wordMap.put("shit", "shoot");
        wordMap.put("friend", "fwend");
        wordMap.put("stop", "stawp");
        wordMap.put("god", "gosh");
        wordMap.put("dick", "peepee");
        wordMap.put("penis", "peepee");
        wordMap.put("damn", "darn");
        wordMap.put("kill", "hug");
        wordMap.put("dead", "eepy");
        wordMap.put("sleepy", "eepy");
        wordMap.put("death", "etewnaw west");
        wordMap.put("no", "nuuu");
        wordMap.put("yes", "yus~");
        wordMap.put("you", "u");
        wordMap.put("you're", "uw");
        wordMap.put("your", "uw");
        wordMap.put("me", "mew");
        wordMap.put("my", "mai");
        wordMap.put("cute", "kawaii~");
        wordMap.put("happy", "hewpy");
        wordMap.put("sorry", "sowwy");
        wordMap.put("lol", "teehee");
        wordMap.put("what", "wut");
        wordMap.put("really", "weawwy");
        wordMap.put("little", "wittwe");
    }

    public static String convert(String text, uwuScapeConfig config) {
        if (config.owoifyMode() == OwoifyMode.OFF)
        {
            return text;
        }

        String cleanedForGhostCheck = text
                // remove HTML tags like <col=...>, <br>, etc.
                .replaceAll("(?i)<[^>]+>", " ")
                // replace <br/> with space (if not already handled)
                .replaceAll("(?i)<br\\s*/?>", " ")
                // remove any non-letter / non-space characters (punctuation)
                .replaceAll("[^A-Za-z\\s]", " ")
                .trim()
                .toLowerCase();

        if (!cleanedForGhostCheck.isEmpty() && cleanedForGhostCheck.matches("^(w[o]+)(\\s+w[o]+)*$"))
        {
            // It's ghost speech — build uwu noise without prefixes/emotes
            String[] ghostWords = cleanedForGhostCheck.split("\\s+");
            StringBuilder ghostResult = new StringBuilder();

            for (int i = 0; i < ghostWords.length; i++)
            {
                String w = ghostWords[i];

                // count the number of 'o'
                int oCount = 0;
                for (char c : w.toCharArray())
                {
                    if (c == 'o') oCount++;
                }

                // "wooo" -> "w" + (oCount) * "u"
                StringBuilder newGhost = new StringBuilder("uw");
                for (int j = 0; j < oCount; j++) newGhost.append('u');

                if (i > 0) ghostResult.append(" ");
                ghostResult.append(newGhost);
            }
            if (Math.random() < 0.2 && config.randomEmotes())
            {
                ghostResult.append(
                        emojiSuffixes.get((int)(Math.random() * emojiSuffixes.size()))
                );
            }

            return ghostResult.toString();
        }

        text = text.replaceAll("(?i)<br\\s*/?>", " ");
        String[] words = text.split("\\s+");
        String result = "";

        double roll = Math.floor(Math.random()*7);

        if(roll == 0 && config.randomPrefix()) {
            result += prefixes.get((int) Math.floor(Math.random() * prefixes.size()));
        }


        boolean first = true;

        for(String w : words) {
            String cleanWord = w.replaceAll("[^a-zA-Z]", "").toLowerCase();
            String replaced = wordMap.get(cleanWord);
            if(replaced != null) {
                String punctuation = w.replaceAll("[a-zA-Z]", "");
                result += " " + replaced + punctuation;
            }
            else if (config.owoifyMode() == OwoifyMode.FULL) {
                result += " ";
                for(char c : w.toCharArray()) {
                    char newChar = c;
                    if(c == 'l' || c == 'r') {
                        newChar = 'w';
                    }
                    else if(c == 'L' || c == 'R') {
                        newChar = 'W';
                    }

                    result += newChar;
                }
            } else {
                result += (first ? "" : " ") + w;
            }
            first = false;
        }
        if (Math.random() < 0.2 && config.randomEmotes())
        {
            result += emojiSuffixes.get((int)(Math.random() * emojiSuffixes.size()));
        }
        return result;
    }

    public static String convertMenuText(String text, uwuScapeConfig config) {
        if (config.owoifyMode() == OwoifyMode.OFF) {
            return text;
        }

        // Replace <br> with space just like normal convert
        text = text.replaceAll("(?i)<br\\s*/?>", " ");

        // Regex splits into tags (<...>) or words (\S+)
        Pattern pattern = Pattern.compile("(<[^>]+>|\\S+)");
        Matcher matcher = pattern.matcher(text);

        StringBuilder result = new StringBuilder();
        boolean first = true;

        while (matcher.find()) {
            String token = matcher.group();

            // Preserve tags as-is
            if (token.startsWith("<") && token.endsWith(">")) {
                result.append(token);
            } else {
                // strip non-alpha chars for lookup
                String cleanWord = token.replaceAll("[^a-zA-Z]", "").toLowerCase();
                String punctuation = token.replaceAll("[a-zA-Z]", "");
                String replaced = wordMap.get(cleanWord);

                if (!first) result.append(" "); // preserve spaces

                if (replaced != null) {
                    result.append(replaced).append(punctuation);
                } else if (config.owoifyMode() == OwoifyMode.FULL) {
                    for (char c : token.toCharArray()) {
                        char newChar = c;
                        if (c == 'l' || c == 'r') newChar = 'w';
                        else if (c == 'L' || c == 'R') newChar = 'W';
                        result.append(newChar);
                    }
                } else {
                    result.append(token); // light mode just keeps the word
                }

                first = false;
            }
        }

        return result.toString();
    }
}
