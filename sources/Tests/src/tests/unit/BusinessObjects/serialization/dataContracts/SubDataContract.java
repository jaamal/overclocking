package tests.unit.BusinessObjects.serialization.dataContracts;

import dataContracts.BusinessObject;

public class SubDataContract extends BusinessObject
{
    private int intField;
    private SubSubDataContract subSubDataContractField;

    public int getIntField()
    {
        return intField;
    }

    public void setIntField(int intField)
    {
        this.intField = intField;
    }

    public SubSubDataContract getSubSubDataContractField()
    {
        return subSubDataContractField;
    }

    public void setSubSubDataContractField(SubSubDataContract subSubDataContractField)
    {
        this.subSubDataContractField = subSubDataContractField;
    }
}
