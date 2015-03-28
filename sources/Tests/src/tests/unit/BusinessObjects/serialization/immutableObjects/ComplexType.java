package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class ComplexType extends BusinessObject
{
    public ComplexType(
            String stringValue,
            int intValue,
            SubType subType)
    {
        this.stringValue = stringValue;
        this.intValue = intValue;
        this.subType = subType;
    }
    
    public final String stringValue;
    public final int intValue;
    public final SubType subType;
}
