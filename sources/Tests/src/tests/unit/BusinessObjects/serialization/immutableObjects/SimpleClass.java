package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class SimpleClass extends BusinessObject
{
    public SimpleClass(
            String firstField,
            String secondField)
    {
        this.firstField = firstField;
        this.secondField = secondField;
    }

    public final String firstField;
    public final String secondField;
}
