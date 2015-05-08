package helpers;

import java.util.List;

import dataContracts.FactorDef;

public class FactorizationHelpers
{
    public static String toString(List<FactorDef> factors)
    {
        StringBuffer result = new StringBuffer();
        for (FactorDef factor : factors) {
            if (factor.isTerminal) {
                result.append((char) factor.symbol);
            }
            else {
                String subString = result.substring((int) factor.beginPosition, (int) (factor.beginPosition + factor.length));
                result.append(subString);
            }
        }
        return result.toString();
    }
}
