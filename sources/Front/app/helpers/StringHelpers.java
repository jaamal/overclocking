package helpers;

public class StringHelpers {
	//TODO: strange method
    public static int getHash(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            int symbolCode = str.charAt(i) - '0';
            hash += symbolCode * (1 << symbolCode);
        }

        return hash;
    }
}
