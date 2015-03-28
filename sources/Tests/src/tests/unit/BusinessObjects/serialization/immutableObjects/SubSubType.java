package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class SubSubType extends BusinessObject
{
    public SubSubType(String zzzString)
    {
        this.zzzString = zzzString;
    }
    
    public final String zzzString;
}
