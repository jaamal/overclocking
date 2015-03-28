package tests.unit.Algorithms.patternMatching.arithmeticProgression.table;

import junit.framework.Assert;

import org.junit.Test;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.table.hash.OwnHashTableBasedTable;
import tests.unit.UnitTestBase;

public class HashBasedTableTests extends UnitTestBase
{
    @Test
    public void test()
    {
        OwnHashTableBasedTable table = new OwnHashTableBasedTable(10, 10, 2);
        for (int i = 0; i < 10; ++i)
            table.set(i, i, ArithmeticProgression.create(i));
        for (int i = 0; i < 10; ++i)
            for (int j = 0; j < 10; ++j)
            {
                if (i == j)
                    Assert.assertEquals(ArithmeticProgression.create(i), table.get(i, j));
                else
                    Assert.assertEquals(ArithmeticProgression.Empty, table.get(i, j));
            }

    }
}


