package compressionservice.compression.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;
import compressionservice.compression.algorithms.lz77.windows.IStringWindow;
import compressionservice.compression.algorithms.lz77.windows.IWindowFactory;
import dataContracts.FactorDef;

public class LZ77FactorIterator implements IFactorIterator
{
    private IReadableCharArray charArray;
    private IStringWindow window;
    private long arrayPosition;
    private final long arrayLength;
    private final long windowSize;

    public LZ77FactorIterator(IWindowFactory windowFactory, IReadableCharArray charArray, int windowSize)
    {
        this.charArray = charArray;
        this.arrayPosition = 0;
        this.arrayLength = charArray.length();
        this.windowSize = windowSize;
        this.window = windowFactory.create(windowSize);
        this.arrayPosition = 0;
    }

    @Override
    public FactorDef next()
    {
        if (!any())
            throw new IllegalAccessError("The method called when all factors scanned.");

        long endPosition = (this.arrayLength < this.arrayPosition + this.windowSize)
                ? this.arrayLength
                : this.arrayPosition + this.windowSize;
        IReadableCharArray subString = this.charArray.subArray(this.arrayPosition, endPosition);
        IPlace place = this.window.search(subString);
        FactorDef result;
        String string;
        if (place.getLength() > 0)
        {
            result = new FactorDef(place.getPosition(), place.getLength());
            string = toString(this.charArray, this.arrayPosition, this.arrayPosition + place.getLength());
            this.arrayPosition += place.getLength();
        }
        else
        {
            result = new FactorDef(this.charArray.get(this.arrayPosition));
            string = toString(this.charArray, this.arrayPosition, this.arrayPosition + 1);
            ++this.arrayPosition;
        }
        this.window.append(string);

        return result;
    }

    @Override
    public boolean any()
    {
        return this.arrayPosition < this.arrayLength;
    }

    // TODO: it seems that this method should be at IReadableCharArray since
    // it's realization should know how to get substring
    private static String toString(IReadableCharArray readableCharArray, long start, long end)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (long i = start; i < end; i++)
            stringBuffer.append(readableCharArray.get(i));
        return stringBuffer.toString();
    }

    @Override
    public void close() throws Exception {
    }
}
