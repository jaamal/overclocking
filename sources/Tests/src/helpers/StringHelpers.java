package helpers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class StringHelpers
{
    public static String[] getOwnSubstrings(String text) {
        if (text == null || text.length() == 0)
            return new String[0];
        
        HashSet<String> substringsSet = new HashSet<>();
        for (int i = 1; i < text.length(); i++) {
            for (int pos = 0; pos < text.length() - i; pos++) {
                String candidate = text.substring(pos, pos + i);
                if (!substringsSet.contains(candidate))
                    substringsSet.add(candidate);
            }
        }
        
        String[] result = substringsSet.toArray(new String[0]);
        Arrays.sort(result, Comparator.comparing(String::length));
        return result;
    }
}
