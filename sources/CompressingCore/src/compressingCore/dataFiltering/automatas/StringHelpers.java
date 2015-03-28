package compressingCore.dataFiltering.automatas;

public class StringHelpers
{
    public static int[] applyZBlocksAlgorithm(String string)
    {
        int length = string.length();
        int[] result = new int[length];
        if (length == 0 || length == 1)
            return result;

        result[1] = explicitCompare(string, 0, string, 1);
        int left = 0, right = 1;
        if (result[1] > 0)
        {
            right = result[1] + 1;
            left = 1;
        }

        for (int i = 2; i < length; i++)
        {
            if (i >= right)
            {
                result[i] = explicitCompare(string, 0, string, i);
                if (result[i] > 0)
                {
                    right = i + result[i];
                    left = i;
                }
            } else
            {
                int preimage = i - left;
                if (result[preimage] < right - i)
                    result[i] = result[preimage];
                else
                {
                    int matchLen = explicitCompare(string, right - i, string, right);
                    result[i] = right - i + matchLen;
                    right += matchLen;
                    left = i;
                }
            }
        }
        return result;
    }

    public static int explicitCompare(String str1, int pos1, String str2, int pos2)
    {
        validatePosition(str1, pos1);
        validatePosition(str2, pos2);
        int length = Math.min(str1.length() - pos1, str2.length() - pos2);
        for (int i = 0; i < length; i++)
            if (str1.charAt(pos1 + i) != str2.charAt(pos2 + i))
                return i;
        return length;
    }

    public static void validatePosition(String str, int position)
    {
        if (position < 0 || position > str.length())
            throw new RuntimeException();
    }

    public static int[] calculateSPArray(String string)
    {
        int[] zBlocks = applyZBlocksAlgorithm(string);
        int length = zBlocks.length;
        int[] result = new int[length];
        for (int i = length - 1; i > 0; i--)
        {
            int j = i + zBlocks[i] - 1;
            result[j] = zBlocks[i];
        }
        for (int i = length - 2; i > 0; i--)
            result[i] = Math.max(result[i + 1] - 1, result[i]);

        return result;
    }
}
