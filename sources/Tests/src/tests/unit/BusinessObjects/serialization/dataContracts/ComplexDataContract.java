package tests.unit.BusinessObjects.serialization.dataContracts;

import dataContracts.BusinessObject;

public class ComplexDataContract extends BusinessObject
{
    private String stringField;
    private SubDataContract subDataContractField;

    public String getStringField()
    {
        return stringField;
    }

    public void setStringField(String stringField)
    {
        this.stringField = stringField;
    }

    public SubDataContract getSubDataContractField()
    {
        return subDataContractField;
    }

    public void setSubDataContractField(SubDataContract subDataContractField)
    {
        this.subDataContractField = subDataContractField;
    }
}
