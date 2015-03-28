//package tests.front.configuration;
//
//import java.util.Random;
//
//public class Generator
//{
//	private static final Random generator = new Random();
//	
//	public static String genStr(int length) {
//		byte[] bytes = new byte[length];
//		generator.nextBytes(bytes);
//		return new String(bytes);
//	}
//	
//	public static long genLong() {
//		return generator.nextLong();
//	}
//	
//	public static long genULong() {
//		return Math.abs(genLong());
//	}
//	
//	public static int genInt() {
//		return generator.nextInt();
//	}
//	
//	public static int genUInt() {
//		return Math.abs(genInt());
//	}
//}
