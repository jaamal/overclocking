package commons.utils;

public class MathHelpers
{
    public static double log(int base, long value)
    {
        if (base <= 1)
            throw new AssertionError("A base of logarithm can't be less or equal than one.");
        if (value <= 0)
            throw new AssertionError("The value should be positive.");
        return Math.log10(value) / Math.log10(base);
    }

    /**
     * Calculates the maximum number k, that 2 ^ k <= number     
     */
    public static int getDeg2(long number)
    {
        int result = 0;
        while (number > 1)
        {
            result++;
            number >>= 1;
        }
        return result;
    }

    /**
     * Calculate the minimum number n, that: <br/>
     * 1) n >= number; <br/>
     * 2) n = 2<sup>k</sup> for some number k <br/>
     */
    public static long minDeg2Bigger(long number)
    {
        long result = 1;
        while (result < number)
            result <<= 1;
        return result;
    }
}
