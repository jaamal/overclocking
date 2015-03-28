package tests.unit.Algorithms.patternMatching.arithmeticProgression.table;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.array.ArrayBasedTable;
import patternMatching.fcpm.table.builder.IAPTableBuilder;

import java.util.ArrayList;
import java.util.List;

import static patternMatching.fcpm.arithmeticProgression.ArithmeticProgression.Empty;
import static patternMatching.fcpm.arithmeticProgression.ArithmeticProgression.create;

public class StupidAPTableBuilder implements IAPTableBuilder
{
    @Override
    public IAPTable build(Product[] patternSlp, Product[] textSlp)
    {
        ArrayBasedTable apTable = new ArrayBasedTable(patternSlp.length, textSlp.length);
        for (int i = 0; i < patternSlp.length; ++i)
        {
            String patternText = SLPHelper.getText(patternSlp, i);
            for (int j = 0; j < textSlp.length; ++j)
            {
                String textText = SLPHelper.getText(textSlp, j);
                int cutPosition = textText.length() == 1 ? 0 : textSlp[j].CutPosition;
                apTable.set(i, j, findOccurrences(patternText, textText, cutPosition));
            }
        }

        return apTable;
    }

    private static ArithmeticProgression findOccurrences(String patternText, String textText, int cutPosition)
    {
        int alpha = Math.max(0, cutPosition - patternText.length());
        int beta = Math.min(textText.length(), cutPosition + patternText.length());
        String o = textText.substring(0, beta);

        int first = o.indexOf(patternText, alpha);
        List<Integer> elements = new ArrayList<Integer>();
        while (first != -1)
        {
            elements.add(first);
            first = o.indexOf(patternText, first + 1);
        }

        if (elements.isEmpty())
            return Empty;
        if (elements.size() == 1)
            return create(elements.get(0), 1, 1);
        int dif = elements.get(1) - elements.get(0);
        for (int i = 1; i < elements.size(); ++i)
        {
            if (dif != elements.get(i) - elements.get(i - 1))
                throw new RuntimeException("bad test reality");
        }
        return create(elements.get(0), dif, elements.size());
    }
}
