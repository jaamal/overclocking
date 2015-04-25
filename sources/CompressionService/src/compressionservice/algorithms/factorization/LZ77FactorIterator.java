package compressionservice.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lz77.ITextWindow;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import dataContracts.FactorDef;

public class LZ77FactorIterator implements IFactorIterator
{
    private IReadableCharArray charArray;
    private long arrayPosition;
    private final long arrayLength;
    private ITextWindow textWindow;

    public LZ77FactorIterator(ITextWindow textWindow, IReadableCharArray charArray)
    {
        this.textWindow = textWindow;
        this.charArray = charArray;
        this.arrayPosition = 0;
        this.arrayLength = charArray.length();
        this.arrayPosition = 0;
    }

    @Override
    public FactorDef next()
    {
        if (!any())
            throw new IllegalAccessError("The method called when all factors scanned.");

        long endPosition = (this.arrayLength < this.arrayPosition + this.textWindow.size())
                ? this.arrayLength
                : this.arrayPosition + this.textWindow.size();
        IReadableCharArray subString = this.charArray.subArray(this.arrayPosition, endPosition);
        Location location = this.textWindow.search(subString);
        FactorDef result;
        String string;
        if (location.length > 0)
        {
            result = new FactorDef(location.beginPosition, location.length);
            string = charArray.toString(this.arrayPosition, this.arrayPosition + location.length);
            this.arrayPosition += location.length;
        }
        else
        {
            char symbol = this.charArray.get(this.arrayPosition);
            result = new FactorDef(symbol);
            string = String.valueOf(symbol);
            ++this.arrayPosition;
        }
        this.textWindow.append(string);

        return result;
    }

    @Override
    public boolean any()
    {
        return this.arrayPosition < this.arrayLength;
    }

    @Override
    public void close() throws Exception {
    }
}
