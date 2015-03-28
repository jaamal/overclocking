package patternMatching;

import patternMatching.fcpm.preprocessing.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ParallelismAnalyser {
    private final Product[] text;
    private final Product[] pattern;
    private int[][] table;
    private int[] levelSize;
    private int longestPath;

    public ParallelismAnalyser(Product[] text, Product[] pattern) {
        this.text = text;
        this.pattern = pattern;
        this.table = new int[pattern.length][text.length];
        this.longestPath = 0;
        this.levelSize = new int[text.length];
        doTableBuilldingImitation();
    }

    private void doTableBuilldingImitation(){
        for (int i = 0; i < pattern.length; i++)
            for (int j = 0; j < text.length; j++) {
                if (pattern[i].IsTerminal || text[j].IsTerminal) table[i][j] = 1;
                else {
                    int firstPatternPart = pattern[i].FirstProduct;
                    int secondPatternPart = pattern[i].SecondProduct;
                    int maxDistance = 0;
                    for (int k = 0; k <= j; k++) {
                        maxDistance = Math.max(maxDistance, table[firstPatternPart][k]);
                        maxDistance = Math.max(maxDistance, table[secondPatternPart][k]);
                    }
                    table[i][j] = maxDistance + 1;
                }

                longestPath = Math.max(longestPath, table[i][j]);
            }

        countLevelsSize();
    }

    public int getLongestPath(){
        return longestPath;
    }

    public void countLevelsSize(){
        for (int i = 0; i <= longestPath; i++) levelSize[i] = 0;

        for (int i = 0; i < pattern.length; i++)
            for (int j = 0; j < text.length; j++) {
                levelSize[table[i][j]]++;
            }
    }

    public void logResult(String filename){
        PrintWriter output;

        try {
            output = new PrintWriter(new FileWriter(filename));
            output.println("MaxPathLength = " + this.getLongestPath() +" Text SLP size = " + text.length + " Pattern SLP size = " + pattern.length);

            if (this.getLongestPath() < 100) {
                for (int i = 0; i < this.getLongestPath(); i++)
                    output.println("On " + i + " level "+ this.levelSize[i] + " cells");
            }

            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
