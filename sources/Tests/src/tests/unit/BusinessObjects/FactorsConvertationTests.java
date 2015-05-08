package tests.unit.BusinessObjects;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import tests.unit.UnitTestBase;
import dataContracts.FactorDef;
import dataContracts.LZFactorDef;

public class FactorsConvertationTests extends UnitTestBase
{
    @Test
    public void testConvertArrayListToArray() {
        ArrayList<LZFactorDef> factorsList = new ArrayList<LZFactorDef>();
        factorsList.add(new LZFactorDef(true, 0, 1, 'a'));
        factorsList.add(new LZFactorDef(true, 0, 1, 'b'));
        factorsList.add(new LZFactorDef(false, 0, 2, ' '));
        
        FactorDef[] actuals = factorsList.toArray(new FactorDef[0]);
        Assert.assertEquals(3, actuals.length);
    }
}
