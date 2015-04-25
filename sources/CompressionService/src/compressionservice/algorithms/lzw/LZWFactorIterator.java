package compressionservice.algorithms.lzw;

import compressingCore.dataAccess.IReadableCharArray;

import java.util.HashMap;

public class LZWFactorIterator
{
    private class RW
    {
        private IReadableCharArray source;

        public RW(IReadableCharArray readableCharArray)
        {
            source = readableCharArray;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
                return false;
            RW another = (RW) obj;
            if (source.length() != another.source.length())
                return false;
            for (int index = 0; index < source.length(); ++index)
                if (source.get(index) != another.source.get(index))
                    return false;
            return true;
        }

        @Override
        public int hashCode()
        {
            int result = 0;
            for (int i = 0; i < source.length(); i++)
                result = result * 31415926 + source.get(i);
            return result;
        }
    }

    private IReadableCharArray source;
    private long sourceLength;
    private HashMap<RW, LZWFactor> codeMap;
    private long currentPosition;
    private long currentCode;
    private long previousPosition;
    private long willPosition;

    public LZWFactorIterator(IReadableCharArray source)
    {
        this.source = source;
        codeMap = new HashMap<RW, LZWFactor>();

        currentPosition = 0;
        sourceLength = source.length();
        currentCode = Character.MAX_CODE_POINT;
    }

    public LZWFactor getNextFactor()
    {
        if (!hasFactors())
            throw new IncorrectUsageException("Try getNextFactor, but no factors available");

        LZWFactor result = processCurrentFactor();
        if (currentPosition != 0)
            processPreviousFactor();
        performTransition();
        return result;
    }

    public boolean hasFactors()
    {
        return currentPosition < sourceLength;
    }

    private void performTransition()
    {
        previousPosition = currentPosition;
        currentPosition = willPosition;
        willPosition = 0;
    }

    private void processPreviousFactor()
    {
        long endPosition = currentPosition;
        boolean isFactorProcessed = false;
        do
        {
            endPosition++;
            RW factorCharArray = new RW(source.subArray(previousPosition, endPosition));

            if (!codeMap.containsKey(factorCharArray))
            {
                currentCode++;
                codeMap.put(factorCharArray, new LZWFactor(currentCode));
                isFactorProcessed = true;
            }
        }
        while (!isFactorProcessed && endPosition < willPosition);
    }

    private LZWFactor processCurrentFactor()
    {
        willPosition = currentPosition + 1;
        boolean isFactorProcessed = false;
        LZWFactor resultFactor = getFirstFactor();
        if (!codeMap.containsValue(resultFactor))
        {
            IReadableCharArray symbolCharArray = source.subArray(currentPosition, currentPosition + 1);
            codeMap.put(new RW(symbolCharArray), resultFactor);
            isFactorProcessed = true;
        }

        while (!isFactorProcessed && willPosition < sourceLength)
        {
            IReadableCharArray factorCharArray = source.subArray(currentPosition, willPosition + 1);
            if (codeMap.containsKey(new RW(factorCharArray)))
            {
                resultFactor = codeMap.get(new RW(factorCharArray));
                willPosition++;
            } else
                isFactorProcessed = true;
        }
        return resultFactor;
    }

    private LZWFactor getFirstFactor()
    {
        char symbol = source.get(currentPosition);
        long code = symbol;
        return new LZWFactor(code);
    }
}
