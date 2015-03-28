package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class ClassWithPrimitives extends BusinessObject
{
    public ClassWithPrimitives(
            int intField,
            long longField,
            char charField,
            boolean booleanValue,
            byte byteValue,
            short shortValue)
    {
        this.intField = intField;
        this.longField = longField;
        this.charField = charField;
        this.booleanValue = booleanValue;
        this.byteValue = byteValue;
        this.shortValue = shortValue;
        stringValue = "zzz";
    }

    public final int intField;
    public final long longField;
    public final char charField;
    public final boolean booleanValue;
    public final byte byteValue;
    public final short shortValue;
    public final String stringValue;
}
