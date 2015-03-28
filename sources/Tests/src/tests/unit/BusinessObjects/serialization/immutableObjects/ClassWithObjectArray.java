package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class ClassWithObjectArray extends BusinessObject
{
    public ClassWithObjectArray(SimpleClass[] simpleClassArray)
    {
        this.simpleClassArray = simpleClassArray;
    }

    public final SimpleClass[] simpleClassArray;
}
