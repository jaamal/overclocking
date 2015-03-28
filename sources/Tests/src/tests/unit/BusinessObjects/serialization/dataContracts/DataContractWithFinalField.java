package tests.unit.BusinessObjects.serialization.dataContracts;

import dataContracts.BusinessObject;

public class DataContractWithFinalField extends BusinessObject
{
    public final int intField;
    private String stringField;

    public DataContractWithFinalField(int intField)
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
