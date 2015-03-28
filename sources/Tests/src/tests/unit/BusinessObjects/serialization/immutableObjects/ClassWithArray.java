package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class ClassWithArray extends BusinessObject
{
    public ClassWithArray(int[] intArray)
    {
        this.intArray = intArray;
    }
    
    public final int[] intArray;
}
