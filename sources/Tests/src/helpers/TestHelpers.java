package helpers;

import java.util.Random;

public class TestHelpers {
    private static Random random = new Random(43);
    
    public static String genString(int length, int alphabiteSize) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++) {
            result.append((char) ('a' + random.nextInt(alphabiteSize)));
        }
        return result.toString();
    }
    
    public static String genString(int length) {
        return genString(length, 26);
    }
    
    public static int genInt() {
        return random.nextInt();
    }
}
