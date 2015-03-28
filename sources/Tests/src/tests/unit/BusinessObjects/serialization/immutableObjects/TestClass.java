package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class TestClass extends BusinessObject
{
    public TestClass(String stringField)
    {
        this.stringField = stringField;
    }

    public final String stringField;
}
