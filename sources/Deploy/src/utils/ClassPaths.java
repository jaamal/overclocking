package utils;

import java.nio.file.Path;
import java.util.HashSet;

public class ClassPaths {
    public static HashSet<String> getProjectReferences(Path projectClassPath) {
        HashSet<String> result = new HashSet<String>();
        String[] lines = Files.readAllLines(projectClassPath);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("src") && lines[i].contains("path=\"/")) {
                String suffix = lines[i].split("path=\"/")[1];
                String projectName = suffix.split("\"/>")[0];
                if (!result.contains(projectName))
                    result.add(projectName);
            }
        }
        return result;
    }
    
    public static HashSet<String> getProjectJars(Path projectClassPath) {
        HashSet<String> result = new HashSet<String>();
        String[] lines = Files.readAllLines(projectClassPath);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("lib")) {
                String suffix = lines[i].split("path=\"")[1];
                String projectName = suffix.split("\"/>")[0];
                if (!result.contains(projectName))
                    result.add(projectName);
            }
        }
        return result;
    }
}
