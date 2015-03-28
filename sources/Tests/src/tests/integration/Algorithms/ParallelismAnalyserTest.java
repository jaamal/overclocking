package tests.integration.Algorithms;

import helpers.FileHelpers;

import java.util.Random;

import org.junit.Test;

import patternMatching.ParallelismAnalyser;
import patternMatching.fcpm.preprocessing.Product;
import tests.integration.IntegrationTestBase;
import tests.unit.Algorithms.patternMatching.SLPBuildHelper;

import commons.settings.ISettings;

public class ParallelismAnalyserTest extends IntegrationTestBase {

    private SLPBuildHelper slpBuildHelper;

    @Override
    public void setUp() {
        super.setUp();
        slpBuildHelper = container.create(SLPBuildHelper.class);
    }

    @Test
    public void randomStringTest(){
        String text = getRandomString(30000);
        Product[] patternSLP = slpBuildHelper.buildSlp(container.get(ISettings.class), text.substring(100, 1000));
        Product[] textSLP = slpBuildHelper.buildSlp(container.get(ISettings.class), text);

        ParallelismAnalyser analyser = new ParallelismAnalyser(textSLP, patternSLP);
        analyser.logResult("parallel_RandomString_26letterAlphabet");
    }

    @Test
    public void testAAES() {
        String s = new String(FileHelpers.readDNA(container, "AAES.gz"));
        String dna = s;

        Product[] patternSLP = slpBuildHelper.buildSlp(container.get(ISettings.class), dna.substring(100, 1000));
        Product[] textSLP = slpBuildHelper.buildSlp(container.get(ISettings.class), dna);

        ParallelismAnalyser analyser = new ParallelismAnalyser(textSLP, patternSLP);
        analyser.logResult("parallel_AAES");
    }

    @Test
    public void testAAESinItself() {
        String s = new String(FileHelpers.readDNA(container, "AAES.gz"));
        String dna = s;

        Product[] patternSLP = slpBuildHelper.buildSlp(container.get(ISettings.class), dna);
        Product[] textSLP = slpBuildHelper.buildSlp(container.get(ISettings.class), dna);

        ParallelismAnalyser analyser = new ParallelismAnalyser(textSLP, patternSLP);
        analyser.logResult("parallel_AAESinItself");
    }

    @Test
    public void testAATT() {
        String s = new String(FileHelpers.readDNA(container, "AATT.gz"));
        String dna = s;

        Product[] patternSLP = slpBuildHelper.buildSlp(container.get(ISettings.class), dna.substring(1000, 5000));
        Product[] textSLP = slpBuildHelper.buildSlp(container.get(ISettings.class), dna);

        ParallelismAnalyser analyser = new ParallelismAnalyser(textSLP, patternSLP);
        analyser.logResult("parallel_AATT");
    }

    private static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            builder.append((char) (random.nextInt(25) + 'a'));
        }
        return builder.toString();
    }


}

