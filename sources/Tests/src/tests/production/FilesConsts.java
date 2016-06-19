package tests.production;

import java.nio.file.Paths;

public class FilesConsts
{
    private static String filesFolderPath = Paths.get(System.getProperty("user.dir"), "..", "..", "..", "..", "data").toString();
    public static String wgsFolderPath = Paths.get(filesFolderPath, "dna").toString();
    public static String randomFolderPath = Paths.get(filesFolderPath, "random").toString();
}
