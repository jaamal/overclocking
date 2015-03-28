package storage;

public class ColumnFormatter
{
    public static String asPositiveInt(int value)
    {
        if (value < 0)
            throw new IllegalArgumentException("Expected value >=0, but was " + value);
        String valueString = value + "";
        String template = "0000000000";
        return template.substring(valueString.length()) + valueString;
    }
}
