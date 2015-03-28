package tests.unit.BusinessObjects.serialization.dataContracts;

import dataContracts.BusinessObject;

public class SubSubDataContract extends BusinessObject
{
    private boolean booleanField;

    public boolean isBooleanField()
    {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField)
    {
        this.booleanField = booleanField;
    }
}
