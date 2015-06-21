package tests.unit.CompressingCore.helpers.compressingCore.dataFiltering.automatas;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tests.unit.UnitTestBase;
import data.filters.automatas.AlphabetFactory;
import data.filters.automatas.AutomataFactory;
import data.filters.automatas.AutomataType;
import data.filters.automatas.IAutomata;
import data.filters.automatas.IAutomataFactory;

public class WordsBoundedAutomataIntegrationTest extends UnitTestBase
{
    private final static String input = "FEATURES Location/Qualifiers source 1..5093554" +
            "/organism='Bacillus anthracis A2012' /mol_type='genomic DNA' /db_xref='taxon:191218'" +
            "BASE COUNT 1630300 a 897050 c 881962 g 1640026 t" +
            "ORIGIN 1 aagtttttta atttcttttt tgtcgttttc tgcgtttctg catcagcgac ggttattaat" +
            "//";

    @Test
    public void integrationTest()
    {
        IAutomataFactory factory = new AutomataFactory(new AlphabetFactory());
        IAutomata automata = factory.createAutomata(AutomataType.DNA);
        String actual = getAcceptedSymbols(automata, input);
        assertEquals("aagttttttaatttcttttttgtcgttttctgcgtttctgcatcagcgacggttattaat", actual);
    }

    private static String getAcceptedSymbols(IAutomata automata, String input)
    {
        String result = "";
        for (char symbol : input.toCharArray())
        {
            automata.move(symbol);
            if (automata.isAcceptState())
                result += symbol;
        }
        return result;
    }
}