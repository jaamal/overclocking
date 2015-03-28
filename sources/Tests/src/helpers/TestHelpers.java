package helpers;

import java.util.Random;

public class TestHelpers {
        public static String generateRandomString(Random rnd, int length, int charsCount) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++) {
            result.append((char) ('a' + rnd.nextInt(charsCount)));
        }
        return result.toString();
    }
    
    private static Random random = new Random(43);
    
    public static String genString(int length) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++) {
            result.append((char) ('a' + random.nextInt(26)));
        }
        return result.toString();
    }
    
    public static int genInt() {
        return random.nextInt();
    }
}
