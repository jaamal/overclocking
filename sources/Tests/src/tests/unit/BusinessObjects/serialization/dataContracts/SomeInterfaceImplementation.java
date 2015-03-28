package tests.unit.BusinessObjects.serialization.dataContracts;

import dataContracts.BusinessObject;

public class SomeInterfaceImplementation extends BusinessObject implements ISomeInterface
{
    private int intField;
    private String stringField;

    public int getIntField()
    {
        return intField;
    }

    public void setIntField(int intField)
    {
        this.intField = intField;
    }

    public String getStringField()
    {
        return stringField;
    }

    public void setStringField(String stringField)
    {
        this.stringField = stringField;
    }
}
