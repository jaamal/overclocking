package helpers;

import java.util.ArrayList;
import java.util.Random;

import dataContracts.FactorDef;
import dataContracts.LZFactorDef;

public class FactorizationScenarios
{
    private static Random random = new Random(42);
    
    public static LZFactorDef[] generate(int totalLen) {
        ArrayList<LZFactorDef> result = new ArrayList<LZFactorDef>();
        int currentLen = 0;
        while (currentLen < totalLen) {
            boolean isTerminal = (currentLen <= 3 || random.nextInt(10) == 0);
            if (isTerminal) {
                result.add(new LZFactorDef((char) ('a' + random.nextInt(26))));
                currentLen++;
            } else {
                int beginPosition = random.nextInt(currentLen);
                int length = 1 + random.nextInt(Math.min(300, Math.min(totalLen - currentLen, currentLen - beginPosition)));
                result.add(new LZFactorDef(beginPosition, length));
                currentLen += length;
            }
        }
        return result.toArray(new LZFactorDef[0]);
    }
    
    public static FactorDef[] generate2(int totalLen) {
        ArrayList<FactorDef> result = new ArrayList<FactorDef>();
        int currentLen = 0;
        while (currentLen < totalLen) {
            boolean isTerminal = (currentLen <= 3 || random.nextInt(10) == 0);
            if (isTerminal) {
                result.add(new FactorDef((char) ('a' + random.nextInt(26))));
                currentLen++;
            } else {
                int beginPosition = random.nextInt(currentLen);
                int length = 1 + random.nextInt(Math.min(300, Math.min(totalLen - currentLen, currentLen - beginPosition)));
                result.add(new FactorDef(beginPosition, length));
                currentLen += length;
            }
        }
        return result.toArray(new FactorDef[0]);
    }
    
    public static <T extends FactorDef> String stringify(T[] factors) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < factors.length; i++) {
            T factor = factors[i];
            if (factor.isTerminal)
                buffer.append((char) factor.symbol);
            else
                buffer.append(buffer.substring((int) factor.beginPosition, (int) (factor.beginPosition + factor.length)));
        }
        return buffer.toString();
    }
}
