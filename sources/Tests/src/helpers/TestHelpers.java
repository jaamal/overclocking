package helpers;

import java.util.Random;

import avlTree.nodes.AvlTreeNode;

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
    
    public static byte[] genBytes(int length) {
        byte[] result = new byte[length];
        random.nextBytes(result);
        return result;
    }
    
    public static int[] genIntArray(int length) {
        int[] result = new int[length];
        for (int i = 0; i < length; ++i) {
            result[i] = random.nextInt();
        }
        return result;
    }
    
    public static long[] genLongArray(int length) {
        long[] result = new long[length];
        for (int i = 0; i < length; ++i) {
            result[i] = random.nextLong();
        }
        return result;
    }
    
    public static char[] genCharArray(int length) {
        return genString(length).toCharArray();
    }
    
    public static AvlTreeNode genAvlTreeNode() {
        return new AvlTreeNode(random.nextLong(), random.nextLong(), random.nextLong(), 
                               random.nextLong(), random.nextLong(), random.nextLong(), 
                               random.nextLong(), random.nextBoolean());
    }
    
    public static AvlTreeNode[] genAvlTreeNodeArray(int length) {
        AvlTreeNode[] result = new AvlTreeNode[length];
        for (int i = 0; i < length; ++i) {
            result[i] = genAvlTreeNode();
        }
        return result;
    }
    
    public static int genInt() {
        return random.nextInt();
    }
    
    public static long genLong() {
        return random.nextLong();
    }
}
