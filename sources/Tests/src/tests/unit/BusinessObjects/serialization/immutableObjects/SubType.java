package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class SubType extends BusinessObject
{
    public SubType(
            boolean booleanValue,
            long longValue,
            SubSubType subSubTypeValue)
    {
        this.booleanValue = booleanValue;
        this.longValue = longValue;
        this.subSubTypeValue = subSubTypeValue;
    }

    public final boolean booleanValue;
    public final long longValue;
    public final SubSubType subSubTypeValue;
}
